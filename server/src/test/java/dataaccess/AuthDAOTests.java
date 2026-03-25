package dataaccess;

import model.AuthData;
import model.DataAccessException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTests {
    private AuthDAO authDAO;
    @BeforeEach
    public void setup () throws DataAccessException {
        authDAO = new MySqlAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createSuccess () {
        AuthData authData = new AuthData("authToken","user");
        assertDoesNotThrow(() -> {authDAO.createAuth(authData);});
    }
    @Test
    public void createFail () {
        assertThrows(Exception.class,()->{authDAO.createAuth(null);});
    }
    @Test
    public void getSuccess () throws DataAccessException {
        AuthData authData = new AuthData("authToken","user");
        authDAO.createAuth(authData);
        assertNotNull(authDAO.getAuth("authToken"));
    }
    @Test
    public void getFail () throws DataAccessException {
        AuthData authData = new AuthData("authToken","user");
        authDAO.createAuth(authData);
        assertNull(authDAO.getAuth("badauthToken"));
    }
    @Test
    public void deleteSuccess () throws DataAccessException {
        AuthData authData = new AuthData("authToken","user");
        authDAO.createAuth(authData);
        authDAO.deleteAuth("authToken");
        assertNull(authDAO.getAuth("authToken"));
    }
    @Test
    public void deleteFail () throws DataAccessException {
        AuthData authData = new AuthData("authToken","user");
        authDAO.createAuth(authData);
        authDAO.deleteAuth("badauthToken");
        assertNotNull(authDAO.getAuth("authToken"));
    }
    @Test
    public void clear() throws DataAccessException {
        AuthData authData = new AuthData("authToken","user");
        authDAO.createAuth(authData);
        authDAO.clear();
        assertNull(authDAO.getAuth("authToken"));
    }

}
