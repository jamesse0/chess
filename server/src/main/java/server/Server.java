package server;
import handler.ClearHandler;
import handler.GameHandler;
import handler.UserHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import dataaccess.*;
import io.javalin.*;

import java.util.Map;

import static dataaccess.DatabaseManager.configureDatabase;

public class Server {

    private final Javalin javalin;
    private final ClearHandler clearHandler;
    private final UserHandler userHandler;
    private final GameHandler gameHandler;
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
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        javalin.post("/user", userHandler::registerHandler)
                .delete("/db", clearHandler::handleClear)
                .post("/session", userHandler::loginHandler)
                .delete("/session", userHandler::logoutHandler)
                .get("/game", gameHandler::listGamesHandler)
                .post("/game", gameHandler::createGameHandler)
                .put("/game", gameHandler::joinGameHandler);
                //.exception(Exception.class, (error,ctx) -> {ctx.status(500);
                    //ctx.json(Map.of("message", "Error: " + error.getMessage()));});

        // Register your endpoints and exception handlers here.

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
