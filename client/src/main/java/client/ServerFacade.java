package client;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade (String url) {
        serverUrl = url;
    }

    public RegisterResult register (RegisterRequest request) throws DataAccessException {
        var httpRequest = buildRequest("POST", "/user",request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, RegisterResult.class);
    }

    public RegisterResult login (LoginRequest request) throws DataAccessException {
        var httpRequest = buildRequest("POST", "/session",request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, RegisterResult.class);
    }

    public ClearResult logout (LogoutRequest request) throws DataAccessException {
        var httpRequest = HttpRequest.newBuilder()
                .uri((URI.create(serverUrl+"/session")))
                .header("authorization", request.authToken())
                .DELETE()
                .build();
        var response = sendRequest(httpRequest);
        return handleResponse(response, ClearResult.class);
    }

    public ListGamesResult listGames (LogoutRequest request) throws DataAccessException {
        var httpRequest = HttpRequest.newBuilder()
                .uri((URI.create(serverUrl+"/game")))
                .header("authorization", request.authToken())
                .GET()
                .build();
        var response = sendRequest(httpRequest);
        return handleResponse(response, ListGamesResult.class);
    }

    public CreateGameResult createGame (CreateGameRequest request) throws DataAccessException {
        var httpRequest = buildRequest("POST", "/game", request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, CreateGameResult.class);
    }

    public ClearResult joinGame (JoinGameRequest request) throws DataAccessException {
        var httpRequest = buildRequest("PUT","/game", request);
        var response = sendRequest(httpRequest);
        return handleResponse(response, ClearResult.class);
    }

    public ClearResult clear () throws DataAccessException {
        var httpRequest = buildRequest("DELETE", "/db", null);
        var response = sendRequest(httpRequest);
        return handleResponse(response, ClearResult.class);
    }

    private HttpRequest buildRequest (String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl+path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody (Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        }
        else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest (HttpRequest request) throws DataAccessException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception error) {
            throw new DataAccessException("Error: " + error.getMessage());
        }
    }

    private <T> T handleResponse (HttpResponse<String> response, Class<T> responseClass) throws DataAccessException {
        var status = response.statusCode();
        if (status != 200) {
            var body = response.body();
            if (body != null) {
                throw new DataAccessException("Error: " + new Gson().fromJson(body, responseClass));
            }
            throw new DataAccessException("Error: handle response");
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }
}
