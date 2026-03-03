package handler;
import service.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

public class UserHandler {
    private final UserService userService;
    Gson serializer = new Gson();
    public UserHandler(UserService userService) {
        this.userService = userService;
    }
    public void registerHandler(Context ctx) {
        try {
            RegisterRequest request;
            request = serializer.fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result;
            result = userService.registerService(request);
            Responder.success(ctx,result);
        }
        catch (DataAccessException error) {
            Responder.fail(ctx, error);
        }

    }

    public void loginHandler (Context ctx) {
        try {
            LoginRequest request;
            request = serializer.fromJson(ctx.body(), LoginRequest.class);
            RegisterResult result;
            result = userService.loginService(request);
            Responder.success(ctx, result);
        }
        catch (DataAccessException error) {
            Responder.fail(ctx, error);
        }
    }

    public void logoutHandler (Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            LogoutRequest request = new LogoutRequest(authToken);
            ClearResult result = userService.logoutService(request);
            Responder.success(ctx, result);
        }
        catch (DataAccessException error) {
            Responder.fail(ctx, error);
        }
    }
}
