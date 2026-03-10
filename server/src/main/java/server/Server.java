package server;
import handler.ClearHandler;
import handler.GameHandler;
import handler.UserHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import dataaccess.*;
import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private final ClearHandler clearHandler;
    private final UserHandler userHandler;
    private final GameHandler gameHandler;
    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        AuthDAO authDAO = null;
        UserDAO userDAO = null;
        GameDAO gameDAO = null;
        try {
            authDAO = new MySqlAuthDAO();
            userDAO = new MySqlUserDAO();
            gameDAO = new MemoryGameDAO();
        } catch (DataAccessException error) {
            System.exit(1);
        }
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

        // Register your endpoints and exception handlers here.

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
