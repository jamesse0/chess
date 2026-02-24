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
        RegisterRequest request = new RegisterRequest(null,null,null);
        request = serializer.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = null;
        result = userService.RegisterService(request);
        ctx.status(200);
        ctx.json(result);

    }

}
