package client;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import model.DataAccessException;
import websocket.commands.FullUserGameCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;
    public WebSocketFacade (String url, NotificationHandler notificationHandler) throws Exception {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
               @Override
               public void onMessage (String message) {
                   ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                   switch (serverMessage.getServerMessageType()) {
                       case LOAD_GAME -> {
                           LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                           notificationHandler.notify(loadGameMessage);
                       }
                       case ERROR -> {
                           ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                           notificationHandler.notify(errorMessage);
                       }
                       case NOTIFICATION -> {
                           NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                           notificationHandler.notify(notificationMessage);
                       }
                   }
               }
            });
        } catch (Exception error) {
            throw new DataAccessException(error.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(Integer gameID, FullUserGameCommand.PlayerType playerType,
                        String username, String color, String authToken) throws DataAccessException {
        try{
            FullUserGameCommand connectCommand = new FullUserGameCommand
                (UserGameCommand.CommandType.CONNECT, authToken,gameID,username,playerType,color);
            session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void leave(Integer gameID, FullUserGameCommand.PlayerType playerType,
                        String username, String color, String authToken) throws DataAccessException {
        try{
            FullUserGameCommand leaveCommand = new FullUserGameCommand
                    (UserGameCommand.CommandType.LEAVE, authToken,gameID,username,playerType,color);
            session.getBasicRemote().sendText(new Gson().toJson(leaveCommand));
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void resign(Integer gameID, FullUserGameCommand.PlayerType playerType,
                        String username, String color, String authToken) throws DataAccessException {
        try{
            FullUserGameCommand resignCommand = new FullUserGameCommand
                    (UserGameCommand.CommandType.RESIGN, authToken,gameID,username,playerType,color);
            session.getBasicRemote().sendText(new Gson().toJson(resignCommand));
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void makeMove(Integer gameID, FullUserGameCommand.PlayerType playerType,
                         String username, String color, String authToken, ChessMove chessMove)
                            throws DataAccessException {
        try{
            FullUserGameCommand moveCommand = new FullUserGameCommand
                    (UserGameCommand.CommandType.MAKE_MOVE, authToken,gameID,username,playerType,color);
            moveCommand.setChessMove(chessMove);
            session.getBasicRemote().sendText(new Gson().toJson(moveCommand));
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
