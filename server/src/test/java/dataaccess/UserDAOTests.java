package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests {
    private UserDAO userDAO;

    @BeforeEach
    public void setup () throws DataAccessException {
        userDAO = new MySqlUserDAO();
        userDAO.clear();
    }

    @Test
    public void createSuccess () throws DataAccessException{
        UserData userData = new UserData("user", "pass", "email");
        assertDoesNotThrow(() -> {userDAO.createUser(userData);});
    }

    @Test
    public void createFail () throws DataAccessException {
        assertThrows(Exception.class, ()-> {userDAO.createUser(null);});
    }

    @Test
    public void getSuccess () throws DataAccessException {
        UserData userData = new UserData("user", "pass", "email");
        userDAO.createUser(userData);
        assertNotNull(userDAO.getUser("user"));
    }

    @Test
    public void getFail () throws DataAccessException {
        UserData userData = new UserData("user", "pass", "email");
        userDAO.createUser(userData);
        assertNull(userDAO.getUser("baduser"));
    }

    @Test
    public void clear() throws DataAccessException {
        UserData userData = new UserData("user", "pass", "email");
        userDAO.createUser(userData);
        userDAO.clear();
        assertNull(userDAO.getUser("user"));
    }
}
