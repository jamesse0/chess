package websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.FullUserGameCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

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
            (Integer gameID, Session session, String playerType, String username, String color) throws IOException {
        connections.add(gameID, session);
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage("Player " + username + "joined as " + color + " " + playerType);
        connections.broadcast(session, gameID, message);
    }

    private void leave
            (Integer gameID, Session session, String playerType, String username, String color) throws IOException {
        ServerMessage message = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        message.setMessage(String.format("%s: %s has left the game.", playerType, username));
        connections.broadcast(session, gameID, message);
        connections.remove(gameID, session);

    }


}
