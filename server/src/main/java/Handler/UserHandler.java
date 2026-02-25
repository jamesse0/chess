package Handler;
import Service.RegisterRequest;
import Service.RegisterResult;
import Service.UserService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

public class UserHandler {
    private final UserService userService;
    public UserHandler(UserService userService) {
        this.userService = userService;
    }
    public void registerHandler(Context ctx) throws DataAccessException {
        var serializer = new Gson();
        RegisterRequest request;
        request = serializer.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result;
        result = userService.RegisterService(request);
        String resultString = serializer.toJson(result);
        ctx.contentType("application/json");
        ctx.status(200);
        ctx.json(resultString);

    }

}
