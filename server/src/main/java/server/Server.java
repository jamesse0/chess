package server;
import handler.ClearHandler;
import handler.GameHandler;
import handler.UserHandler;
import model.DataAccessException;
import service.ClearService;
import service.GameService;
import service.UserService;
import dataaccess.*;
import io.javalin.*;
import websocket.WebSocketHandler;

import static dataaccess.DatabaseManager.configureDatabase;

public class Server {

    private final Javalin javalin;
    private final ClearHandler clearHandler;
    private final UserHandler userHandler;
    private final GameHandler gameHandler;
    private final WebSocketHandler webSocketHandler;
    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        try {
            configureDatabase();
        } catch (DataAccessException error) {
            error.printStackTrace();
            System.err.println("Database Failure");
        }
        AuthDAO authDAO = new MySqlAuthDAO();
        UserDAO userDAO = new MySqlUserDAO();
        GameDAO gameDAO = new MySqlGameDAO();
        UserService userService = new UserService(authDAO, userDAO);
        ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
        GameService gameService = new GameService(gameDAO, authDAO);
        clearHandler = new ClearHandler(clearService);
        userHandler = new UserHandler(userService);
        gameHandler = new GameHandler(gameService);
        webSocketHandler = new WebSocketHandler(gameService);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        javalin.post("/user", userHandler::registerHandler)
                .delete("/db", clearHandler::handleClear)
                .post("/session", userHandler::loginHandler)
                .delete("/session", userHandler::logoutHandler)
                .get("/game", gameHandler::listGamesHandler)
                .post("/game", gameHandler::createGameHandler)
                .put("/game", gameHandler::joinGameHandler)
                .ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });

        // Register your endpoints and exception handlers here.

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
