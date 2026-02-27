package service;
import Service.UserService;
import Service.RegisterRequest;
import Service.RegisterResult;
import Service.LoginRequest;
import Service.LogoutRequest;
import dataaccess.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class UserServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;
    @BeforeEach
    public void setup () throws DataAccessException {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(authDAO, userDAO);
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    public void validRegister () throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "password", "email");
        RegisterResult result = userService.RegisterService(request);
        assertNotNull(result.authToken());
    }

    @Test
    public void alreadyTaken() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "password", "email");
        RegisterResult result = userService.RegisterService(request);
        RegisterRequest duplicate = new RegisterRequest("user", "password", "email");
        assertThrows(DataAccessException.class, () -> {userService.RegisterService(duplicate);});
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "password", "email");
        userService.RegisterService(request);
        LoginRequest login = new LoginRequest("user", "password");
        RegisterResult result = userService.LoginService(login);
        assertNotNull(result.authToken());
    }

    @Test
    public void badPassword () throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "password", "email");
        userService.RegisterService(request);
        LoginRequest login = new LoginRequest("user", "badpassword");
        assertThrows(DataAccessException.class, () -> {userService.LoginService(login);});
    }

    @Test
    public void logoutSuccess () throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "password", "email");
        RegisterResult result = userService.RegisterService(request);
        userService.LogoutService(new LogoutRequest(result.authToken()));
        assertNull(authDAO.getAuth(result.authToken()));
    }

    @Test
    public void logoutFail () throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "password", "email");
        RegisterResult result = userService.RegisterService(request);
        assertThrows(DataAccessException.class, () -> {userService.LogoutService(new LogoutRequest("badAuthToken"));});
    }
}
