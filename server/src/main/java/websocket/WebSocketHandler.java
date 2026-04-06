package websocket;

import chess.*;
import com.google.gson.Gson;
import io.javalin.websocket.*;
import model.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import websocket.commands.FullUserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;

    public WebSocketHandler (GameService gameService) {
        this.gameService = gameService;
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
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        try {
            FullUserGameCommand command = new Gson().fromJson(ctx.message(), FullUserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> {
                }
                case MAKE_MOVE -> {
                }
                case LEAVE -> {
                }
                case RESIGN -> {
                }
            }
        }
    }

    private void resign (Integer gameID, Session session, String playerType,
                         String username, String color, String authToken) throws Exception {
        if (playerType.equals("Observer")) {
            ServerMessage observer = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            observer.setMessage("Error: As an observer you do not need to resign. Just use the 'leave' command.");
            session.getRemote().sendString(new Gson().toJson(observer));
        }
        else {
            ServerMessage resign = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            resign.setMessage(username+" has resigned. The Game is now over.");
            resign.setGameOver(true);
            connections.broadcast(session, gameID, resign);
        }
    }

    private void connect
            (Integer gameID, Session session, String playerType,
             String username, String color, String authToken) throws IOException, DataAccessException {
        connections.add(gameID, session);
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage("Player " + username + "joined as " + color + " " + playerType);
        GameData gameData = gameService.getGame(authToken, gameID);
        message.setGameState(gameData.game());
        connections.broadcast(session, gameID, message);
        ServerMessage connected = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connected.setMessage("Loading Game...");
        session.getRemote().sendString(new Gson().toJson(connected));
    }

    private void leave
            (Integer gameID, Session session, String playerType, String username, String authToken, String color)
            throws IOException, DataAccessException {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(String.format("%s: %s has left the game.%n", playerType, username));
        connections.broadcast(session, gameID, message);
        connections.remove(gameID, session);
        if (playerType.equals("Player")) {
            GameData gameData = gameService.getGame(authToken,gameID);
            if (color.equals("BLACK")) {
                gameService.updateGame(new GameData(gameData.gameID(),gameData.whiteUsername(),
                        null,gameData.gameName(),gameData.game()),authToken);
            }
            if (color.equals("WHITE")) {
                gameService.updateGame(new GameData(gameData.gameID(),null,
                        gameData.blackUsername(),gameData.gameName(),gameData.game()),authToken);
            }
        }

    }

    private void makeMove (Integer gameID, Session session, String playerType, ChessMove chessMove, String username,
                            String authToken)
                            throws IOException, DataAccessException, InvalidMoveException {
        if (playerType.equals("Observer")) {
            ServerMessage observer = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            observer.setMessage("Error: As an observer you cannot make moves.");
            session.getRemote().sendString(new Gson().toJson(observer));
        }
        else {
            GameData gameData = gameService.getGame(authToken, gameID);
            ChessGame game = gameData.game();
            ChessBoard board = game.getBoard();
            String whitePlayer = gameData.whiteUsername();
            String blackPlayer = gameData.blackUsername();
            ChessPiece piece = board.getPiece(chessMove.getStartPosition());
            String pieceString = switch (piece.getPieceType()) {
                case KING -> "KING";
                case QUEEN -> "QUEEN";
                case BISHOP -> "BISHOP";
                case KNIGHT -> "KNIGHT";
                case ROOK -> "ROOK";
                case PAWN -> "PAWN";
            };
            game.makeMove(chessMove);
            ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            message.setMessage("Loading board...");
            message.setGameState(game);
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            int startRow = chessMove.getStartPosition().getRow();
            int startCol = chessMove.getStartPosition().getColumn();
            int endRow = chessMove.getEndPosition().getRow();
            int endCol = chessMove.getEndPosition().getColumn();
            String[] colHeader = {"a", "b", "c", "d", "e", "f", "g", "h"};
            notify.setMessage(String.format("%s moved %s from %d%s to %d%s%n", username, pieceString, startRow,
                    colHeader[startCol - 1], endRow, colHeader[endCol - 1]));
            gameService.updateGame(gameData, authToken);
            connections.broadcast(session, gameID, notify);
            connections.broadcast(null, gameID, message);
            if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                ServerMessage whiteInCheck = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                whiteInCheck.setMessage(whitePlayer + " is in check.");
                whiteInCheck.setGameOver(true);
                connections.broadcast(null, gameID, whiteInCheck);
            }
            if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                ServerMessage blackInCheck = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                blackInCheck.setMessage(blackPlayer + " is in check.");
                blackInCheck.setGameOver(true);
                connections.broadcast(null, gameID, blackInCheck);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                ServerMessage whiteInCheckmate = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                whiteInCheckmate.setMessage(whitePlayer + " is in checkmate. " + blackPlayer + " wins.");
                whiteInCheckmate.setGameOver(true);
                connections.broadcast(null, gameID, whiteInCheckmate);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                ServerMessage blackInCheckmate = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                blackInCheckmate.setMessage(blackPlayer + " is in checkmate. " + whitePlayer + " wins.");
                blackInCheckmate.setGameOver(true);
                connections.broadcast(null, gameID, blackInCheckmate);
            }

            if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                ServerMessage whiteStalemate = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                whiteStalemate.setMessage(whitePlayer + " is in Stalemate. Game over.");
                whiteStalemate.setGameOver(true);
                connections.broadcast(null, gameID, whiteStalemate);
            }
            if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                ServerMessage blackStalemate = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                blackStalemate.setMessage(whitePlayer + " is in Stalemate. Game over.");
                blackStalemate.setGameOver(true);
                connections.broadcast(null, gameID, blackStalemate);
            }
        }
    }
}
