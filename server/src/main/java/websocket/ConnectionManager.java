package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();

    public void add (Integer gameID, Session session) {
        connections.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void remove (Integer gameID, Session session) {
        if (connections.containsKey(gameID)) {
            Set<Session> game = connections.get(gameID);
            game.remove(session);
        }
    }

    public void broadcast (Session excludeSession, Integer gameID, ServerMessage serverMessage) throws IOException {
        Gson serializer = new Gson();
        if (connections.containsKey(gameID)) {
            Set<Session> game = connections.get(gameID);
            for (Session s: game) {
                if ((s.isOpen()) && (!s.equals(excludeSession))) {
                    s.getRemote().sendString(serializer.toJson(serverMessage));
                }
            }
        }
    }
}
