package server;
import Handler.ClearHandler;
import Handler.GameHandler;
import Handler.UserHandler;
import Service.ClearService;
import Service.GameService;
import Service.UserService;
import dataaccess.*;
import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private final ClearHandler clearHandler;
    private final UserHandler userHandler;
    private final GameHandler gameHandler;
    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
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
                .get("/game", gameHandler::listGamesHandler);
        // Register your endpoints and exception handlers here.

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
