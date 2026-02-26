package Handler;

import Service.ClearResult;
import Service.ClearService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler (ClearService clearService) {
        this.clearService = clearService;
    }

    public void handleClear (Context ctx) throws DataAccessException {
        var serializer = new Gson();
        try {
            ClearResult result = clearService.clearData();
            String resultString = serializer.toJson(result);
            ctx.status(200);
            ctx.contentType("application/json");
            ctx.result(resultString);
        }
        catch (DataAccessException error) {
            ctx.status(500);
        }
    }
}
