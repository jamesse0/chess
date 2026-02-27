package Handler;

import Service.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

public class GameHandler {
    private final GameService gameService;
    Gson serializer = new Gson();

    public GameHandler (GameService gameService) {
        this.gameService = gameService;
    }

    public void listGamesHandler (Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            LogoutRequest request = new LogoutRequest(authToken);
            ListGamesResult result = gameService.listGamesService(request);
            Responder.success(ctx, result);
        }
        catch (DataAccessException error) {
            Responder.fail(ctx, error);
        }
    }

    public void createGameHandler (Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            CreateGameRequest request = serializer.fromJson(ctx.body(), CreateGameRequest.class);
            CreateGameResult result = gameService.createGameService(request, authToken);
            Responder.success(ctx, result);
        }
        catch (DataAccessException error) {
            Responder.fail(ctx, error);
        }
    }
}
