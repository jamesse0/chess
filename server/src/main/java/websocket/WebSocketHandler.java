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
            (Integer gameID, Session session, String playerType, String username) throws IOException {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(String.format("%s: %s has left the game.%n", playerType, username));
        connections.broadcast(session, gameID, message);
        connections.remove(gameID, session);

    }

    private void makeMove (Integer gameID, Session session, String playerType, ChessMove chessMove, String username,
                           String color, String authToken)
                            throws IOException, DataAccessException, InvalidMoveException {
        GameData gameData = gameService.getGame(authToken, gameID);
        ChessGame game = gameData.game();
        ChessBoard board = game.getBoard();
        ChessPiece piece = board.getPiece(chessMove.getStartPosition());
        String pieceString = switch(piece.getPieceType()) {
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
        String[] colHeader = {"a","b","c","d","e","f","g","h"};
        notify.setMessage(String.format("%s moved %s from %d%s to %d%s%n", username, pieceString, startRow,
                            colHeader[startCol-1], endRow, colHeader[endCol-1]));
        gameService.updateGame(gameData, authToken);
        connections.broadcast(session, gameID, notify);
        connections.broadcast(null, gameID, message);
    }
}
