package service;

import dataaccess.AuthDAO;
import model.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.ClearResult;

public class ClearService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public ClearResult clearData () throws DataAccessException {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
        return new ClearResult();
    }
}
