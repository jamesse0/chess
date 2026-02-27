package Handler;

import Service.GameService;
import Service.ListGamesResult;
import Service.LogoutRequest;
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

        }
        catch (DataAccessException error) {

        }
    }
}
