package service;
import Service.ClearService;
import Service.UserService;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class ClearTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private ClearService clearService;
    @BeforeEach
    public void setup () throws DataAccessException {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        clearService = new ClearService(authDAO, userDAO, gameDAO);
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void clearSuccess () throws DataAccessException {
        userDAO.createUser(new UserData("user","password", "email"));
        authDAO.createAuth(new AuthData("authToken", "user"));
        gameDAO.createGame(new GameData(1234,null,null, "game", new ChessGame()));
        clearService.clearData();
        assertNull(userDAO.getUser("user"));
        assertNull(authDAO.getAuth("authToken"));
        assertNull(gameDAO.getGame(1234));
    }
}
