package Handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

public class Responder {
    static Gson serializer = new Gson();

    public static void success(Context ctx, Object result) {
        String resultString = serializer.toJson(result);
        ctx.contentType("application/json");
        ctx.status(200);
        ctx.result(resultString);
    }

    public static void fail(Context ctx, DataAccessException error){
        if (error.getMessage().contains("already taken")) {
            ctx.status(403);
        }
        else if (error.getMessage().contains("bad request")) {
            ctx.status(400);
        }
        else if (error.getMessage().contains("unauthorized")) {
            ctx.status(401);
        }
        else {
            ctx.status(500);
        }
        ErrorResponse eMessage = new ErrorResponse("Error: " + error.getMessage());
        String resultString = serializer.toJson(eMessage);
        ctx.contentType("application/json");
        ctx.result(resultString);
    }
}
