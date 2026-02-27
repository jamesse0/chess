package Service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.GameID;
import model.GameData;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService (GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGamesService (LogoutRequest listReq) throws DataAccessException {
        ListGamesResult games;
        if ((listReq.authToken() == null) ||
                (authDAO.getAuth(listReq.authToken()) == null)) {
            throw new DataAccessException("unauthorized");
        }
        else {
            games = new ListGamesResult(gameDAO.listGames());
        }
        return games;
    }

    public CreateGameResult createGameService (CreateGameRequest gameReq, String authToken) throws DataAccessException {
        CreateGameResult game;
        if ((authToken == null) ||
                (authDAO.getAuth(authToken) == null)) {
            throw new DataAccessException("unauthorized");
        }
        if (gameReq.gameName() == null) {
            throw new DataAccessException("bad request");
        }
        else {
            int gameID = GameID.generateGameID();
            GameData newGame = new GameData(gameID,null,null, gameReq.gameName(), new ChessGame());
            gameDAO.createGame(newGame);
            game = new CreateGameResult(newGame.gameID());
        }
        return game;
    }
}
