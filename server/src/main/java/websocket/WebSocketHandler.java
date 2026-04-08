package websocket;

import chess.*;
import com.google.gson.Gson;
import io.javalin.websocket.*;
import model.AuthData;
import model.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import service.UserService;
import websocket.commands.FullUserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;
    private final UserService userService;

    public WebSocketHandler (GameService gameService, UserService userService) {

        this.gameService = gameService;
        this.userService = userService;
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        System.out.println("WebSocket Closed");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        System.out.println("Websocket Connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        try {
            FullUserGameCommand command = new Gson().fromJson(ctx.message(), FullUserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> {
                    try {
                        connectHandler(command.getGameID(), ctx.session,
                                command.getPlayerTypeString(), command.getUsername(),
                                command.getColor(), command.getAuthToken());
                    } catch (Exception error) {
                        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
                        errorMessage.setErrorMessage("Error: There was an issue connecting. Please try again.");
                        ctx.session.getRemote().sendString(new Gson().toJson(errorMessage));
                    }
                }
                case MAKE_MOVE -> {
                    try {
                        makeMoveHandler(command.getGameID(), ctx.session, command.getChessMove()
                        , command.getUsername(), command.getAuthToken());
                    } catch (Exception error) {
                        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
                        errorMessage.setErrorMessage("Error: Could Not Make Move. Ensure that it is your turn to move" +
                                " and that your move is valid.");
                        ctx.session.getRemote().sendString(new Gson().toJson(errorMessage));
                    }
                }
                case LEAVE -> {
                    try {
                        leaveHandler(command.getGameID(), ctx.session,
                                command.getPlayerTypeString(), command.getUsername(),
                                command.getAuthToken());
                    } catch (Exception error) {
                        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
                        errorMessage.setErrorMessage("Error: There was an issue leaving. Please try again.");
                        ctx.session.getRemote().sendString(new Gson().toJson(errorMessage));
                    }
                }
                case RESIGN -> {
                    try {
                        resignHandler(command.getGameID(), ctx.session,
                                 command.getUsername(), command.getAuthToken());
                    } catch (Exception e) {
                        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
                        errorMessage.setErrorMessage("Error: There was an issue resigning. Please try again.");
                        ctx.session.getRemote().sendString(new Gson().toJson(errorMessage));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Websocket Failure");
        }
    }

    private void resignHandler (Integer gameID, Session session,
                         String username, String authToken) throws Exception {
        AuthData userAuth = userService.getAuth(authToken);
        GameData gameData = gameService.getGame(authToken, gameID);
        ChessGame game = gameData.game();
        String whitePlayer = gameData.whiteUsername();
        String blackPlayer = gameData.blackUsername();
        if ((!userAuth.username().equals(whitePlayer)) && (!userAuth.username().equals(blackPlayer)))  {
            ErrorMessage observer = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
            observer.setErrorMessage("Error: As an observer you do not need to resign. Just use the 'leave' command.");
            session.getRemote().sendString(new Gson().toJson(observer));
        }
        else if (gameService.gameStatus(gameID)) {
            ErrorMessage gameOver = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
            gameOver.setErrorMessage("Error: The game is already over so you do not need to resign. Use the 'leave'" +
                    " command to return to the game menu.");
            session.getRemote().sendString(new Gson().toJson(gameOver));
        }
        else {
            NotificationMessage resign = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            resign.setMessage(username+" has resigned. The Game is now over.");
            gameService.gameOver(gameID);
            connections.broadcast(null, gameID, resign);
        }
    }

    private void connectHandler
            (Integer gameID, Session session, String playerType,
             String username, String color, String authToken) throws IOException, DataAccessException {
        GameData gameData = gameService.getGame(authToken, gameID);
        if (gameData==null) {
            ErrorMessage invalidID = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
            invalidID.setErrorMessage("Error: Invalid GameID");
            session.getRemote().sendString(new Gson().toJson(invalidID));
        }
        else {
            connections.add(gameID, session);
            NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            message.setMessage("Player " + username + "joined as " + color + " " + playerType);
            connections.broadcast(session, gameID, message);
            LoadGameMessage connected = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            connected.setGame(gameData.game());
            session.getRemote().sendString(new Gson().toJson(connected));
        }
    }

    private void leaveHandler
            (Integer gameID, Session session, String playerType, String username, String authToken)
            throws IOException, DataAccessException {
        AuthData userAuth = userService.getAuth(authToken);
        GameData gameData = gameService.getGame(authToken, gameID);
        String whitePlayer = gameData.whiteUsername();
        String blackPlayer = gameData.blackUsername();
        NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(String.format("%s: %s has left the game.%n", playerType, username));
        connections.broadcast(session, gameID, message);
        connections.remove(gameID, session);
        if (userAuth.username().equals(blackPlayer)) {
            gameService.updateGame(new GameData(gameData.gameID(),gameData.whiteUsername(),
                        null,gameData.gameName(),gameData.game()),authToken);
        }
        if (userAuth.username().equals(whitePlayer)) {
            gameService.updateGame(new GameData(gameData.gameID(),null,
                        gameData.blackUsername(),gameData.gameName(),gameData.game()),authToken);
        }


    }

    private void makeMoveHandler (Integer gameID, Session session, ChessMove chessMove,
                                  String username, String authToken)
                            throws IOException, DataAccessException, InvalidMoveException {
        AuthData userAuth = userService.getAuth(authToken);
        GameData gameData = gameService.getGame(authToken, gameID);
        ChessGame game = gameData.game();
        ChessBoard board = game.getBoard();
        String whitePlayer = gameData.whiteUsername();
        String blackPlayer = gameData.blackUsername();
        if ((!userAuth.username().equals(whitePlayer)) && (!userAuth.username().equals(blackPlayer))) {
            ErrorMessage observer = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
            observer.setErrorMessage("Error: As an observer you cannot make moves.");
            session.getRemote().sendString(new Gson().toJson(observer));
        }
        else if (gameService.gameStatus(gameID)) {
            ErrorMessage gameOver = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
            gameOver.setErrorMessage("Error: The game is over so new moves cannot be made. Use the 'leave'" +
                    " command to return to the game menu.");
            session.getRemote().sendString(new Gson().toJson(gameOver));
        }
        else {
            if ((userAuth.username().equals(whitePlayer)) && (game.getTeamTurn()== ChessGame.TeamColor.BLACK)) {
                throw new InvalidMoveException();
            }
            if ((userAuth.username().equals(blackPlayer)) && (game.getTeamTurn()== ChessGame.TeamColor.WHITE)) {
                throw new InvalidMoveException();
            }
            ChessPiece piece = board.getPiece(chessMove.getStartPosition());
            game.makeMove(chessMove);
            String pieceString = switch (piece.getPieceType()) {
                case KING -> "KING";
                case QUEEN -> "QUEEN";
                case BISHOP -> "BISHOP";
                case KNIGHT -> "KNIGHT";
                case ROOK -> "ROOK";
                case PAWN -> "PAWN";
            };
            LoadGameMessage message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            message.setGame(game);
            NotificationMessage notify = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            int startRow = chessMove.getStartPosition().getRow();
            int startCol = chessMove.getStartPosition().getColumn();
            int endRow = chessMove.getEndPosition().getRow();
            int endCol = chessMove.getEndPosition().getColumn();
            String[] colHeader = {"a", "b", "c", "d", "e", "f", "g", "h"};
            notify.setMessage(String.format("%s moved %s from %d%s to %d%s%n", username, pieceString, startRow,
                    colHeader[startCol - 1], endRow, colHeader[endCol - 1]));
            gameService.updateGame(gameData, authToken);
            connections.broadcast(null, gameID, message);
            connections.broadcast(session, gameID, notify);

            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                NotificationMessage whiteInCheckmate = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                whiteInCheckmate.setMessage(whitePlayer + " is in checkmate. " + blackPlayer + " wins.");
                gameService.gameOver(gameID);
                connections.broadcast(null, gameID, whiteInCheckmate);
            }

            else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                NotificationMessage whiteInCheck = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                whiteInCheck.setMessage(whitePlayer + " is in check.");
                gameService.gameOver(gameID);
                connections.broadcast(null, gameID, whiteInCheck);
            }

            else if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                NotificationMessage whiteStalemate = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                whiteStalemate.setMessage(whitePlayer + " is in Stalemate. Game over.");
                gameService.gameOver(gameID);
                connections.broadcast(null, gameID, whiteStalemate);
            }

            if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                NotificationMessage blackInCheck = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                blackInCheck.setMessage(blackPlayer + " is in check.");
                gameService.gameOver(gameID);
                connections.broadcast(null, gameID, blackInCheck);
            }

            else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                NotificationMessage blackInCheckmate = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                blackInCheckmate.setMessage(blackPlayer + " is in checkmate. " + whitePlayer + " wins.");
                gameService.gameOver(gameID);
                connections.broadcast(null, gameID, blackInCheckmate);
            }

            else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                NotificationMessage blackStalemate = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                blackStalemate.setMessage(whitePlayer + " is in Stalemate. Game over.");
                gameService.gameOver(gameID);
                connections.broadcast(null, gameID, blackStalemate);
            }
        }
    }
}
