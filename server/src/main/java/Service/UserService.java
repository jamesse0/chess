package Service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public RegisterResult RegisterService (RegisterRequest regReq) throws DataAccessException {
        if ((regReq.username() == null) || (regReq.password() == null) || (regReq.email() == null)) {
            throw new DataAccessException("bad request");
        }
        UserData userData = new UserData(regReq.username(), regReq.password(), regReq.email());
        RegisterResult result;
        if (userDAO.getUser(userData.username()) != null) {
            throw new DataAccessException("already taken");
        }
        else {
            userDAO.createUser(userData);
            String authToken;
            authToken = AuthToken.generateToken();
            AuthData authData = new AuthData(authToken, userData.username());
            authDAO.createAuth(authData);
            result = new RegisterResult(userData.username(), authData.authToken());
        }
        return result;
    }

    public RegisterResult LoginService (LoginRequest logReq) throws DataAccessException {
        if ((logReq.username() == null) || (logReq.password() == null)) {
            throw new DataAccessException("bad request");
        }
        RegisterResult result;
        if ((userDAO.getUser(logReq.username()) == null)||
                (!Objects.equals(userDAO.getUser(logReq.username()).password(), logReq.password()))) {
            throw new DataAccessException("unauthorized");
        } else {
            String authToken = AuthToken.generateToken();
            AuthData authData = new AuthData(authToken, logReq.username());
            authDAO.createAuth(authData);
            result = new RegisterResult(logReq.username(), authData.authToken());
        }
        return result;
    }

    public ClearResult LogoutService (LogoutRequest logoReq) throws DataAccessException {
        ClearResult result = new ClearResult();
        if ((logoReq.authToken() == null) ||
                (authDAO.getAuth(logoReq.authToken()) == null)) {
            throw new DataAccessException("unauthorized");
        }
        else {
            authDAO.deleteAuth(logoReq.authToken());
        }
        return result;
    }
}
