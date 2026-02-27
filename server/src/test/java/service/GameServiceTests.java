package service;

import Service.GameService;
import Service.LogoutRequest;
import Service.ListGamesResult;
import Service.CreateGameRequest;
import Service.CreateGameResult;
import Service.JoinGameRequest;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private GameService gameService;
    @BeforeEach
    public void setup () throws DataAccessException {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    public void gotLists () throws DataAccessException {
        gameDAO.createGame(new GameData(1234,null,null, "game", new ChessGame()));
        authDAO.createAuth(new AuthData("authToken", "username"));
        LogoutRequest request = new LogoutRequest("authToken");
        ListGamesResult games = gameService.listGamesService(request);
        assertNotNull(games.games());
    }

    @Test
    public void listFail () throws DataAccessException {
        gameDAO.createGame(new GameData(1234,null,null, "game", new ChessGame()));
        authDAO.createAuth(new AuthData("authToken", "username"));
        LogoutRequest request = new LogoutRequest("badauthToken");
        assertThrows(DataAccessException.class, () -> {gameService.listGamesService(request);});
    }

    @Test
    public void createSuccess () throws DataAccessException {
        authDAO.createAuth(new AuthData("authToken", "username"));
        CreateGameRequest request = new CreateGameRequest("game");
        CreateGameResult result = gameService.createGameService(request, "authToken");
        assertNotEquals(0,result.gameID());
    }

    @Test
    public void didNotCreate () throws DataAccessException {
        authDAO.createAuth(new AuthData("authToken", "username"));
        CreateGameRequest request = new CreateGameRequest("game");
        assertThrows(DataAccessException.class, () -> {gameService.createGameService(request, "BadAuth");});
    }

    @Test
    public void joined () throws DataAccessException {
        gameDAO.createGame(new GameData(1234,null,null, "game", new ChessGame()));
        authDAO.createAuth(new AuthData("authToken", "username"));
        JoinGameRequest request = new JoinGameRequest("WHITE", 1234);
        gameService.joinGameService(request,"authToken");
        assertNotNull(gameDAO.getGame(1234).whiteUsername());
    }

    @Test
    public void alreadyTaken () throws DataAccessException {
        gameDAO.createGame(new GameData(1234,null,null, "game", new ChessGame()));
        authDAO.createAuth(new AuthData("authToken", "username"));
        JoinGameRequest request = new JoinGameRequest("WHITE", 1234);
        gameService.joinGameService(request,"authToken");
        JoinGameRequest duplicate = new JoinGameRequest("WHITE", 1234);
        assertThrows(DataAccessException.class, () -> {gameService.joinGameService(duplicate,"authToken");});
    }
}
