package Service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public RegisterResult RegisterService (RegisterRequest regReq) throws DataAccessException {
        UserData userData = new UserData(regReq.username(), regReq.password(), regReq.email());
        RegisterResult result = null;
        if (userDAO.getUser(userData.username()) == null) {
            userDAO.createUser(userData);
            String authToken;
            authToken = AuthToken.generateToken();
            AuthData authData = new AuthData(authToken, userData.username());
            authDAO.createAuth(authData);
            result = new RegisterResult(userData.username(), authData.authToken());
        } else {
            ;
        }
        return result;
    }
}
