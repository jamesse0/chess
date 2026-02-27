package Service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;

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
}
