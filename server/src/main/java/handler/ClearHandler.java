package handler;

import service.ClearResult;
import service.ClearService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler (ClearService clearService) {
        this.clearService = clearService;
    }

    public void handleClear (Context ctx) throws DataAccessException {
        try {
            ClearResult result = clearService.clearData();
            Responder.success(ctx,result);
        }
        catch (DataAccessException error) {
            Responder.fail(ctx,error);
        }
    }
}
