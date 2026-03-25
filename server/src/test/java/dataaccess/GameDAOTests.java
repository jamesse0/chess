package dataaccess;
import chess.ChessGame;
import model.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class GameDAOTests {
    private GameDAO gameDAO;
    @BeforeEach
    public void setup () throws DataAccessException {
        gameDAO = new MySqlGameDAO();
        gameDAO.clear();
    }
    @Test
    public void createSuccess () {
        GameData gameData = new GameData
                (1234,null,null,"game", new ChessGame());
        assertDoesNotThrow(()->{gameDAO.createGame(gameData);});
    }
    @Test
    public void createFail () {
        assertThrows(Exception.class, ()-> {gameDAO.createGame(null);});
    }
    @Test
    public void getSuccess () throws DataAccessException {
        GameData gameData = new GameData
                (1234,null,null,"game", new ChessGame());
        gameDAO.createGame(gameData);
        assertNotNull(gameDAO.getGame(1234));
    }
    @Test
    public void getFail () throws DataAccessException {
        GameData gameData = new GameData
                (1234,null,null,"game", new ChessGame());
        gameDAO.createGame(gameData);
        assertNull(gameDAO.getGame(4321));
    }
    @Test
    public void listSuccess () throws DataAccessException {
        GameData gameData = new GameData
                (1234,null,null,"game", new ChessGame());
        gameDAO.createGame(gameData);
        assertNotEquals(0,gameDAO.listGames().toArray().length);
    }
    @Test
    public void listFail () throws DataAccessException {
        GameData gameData = new GameData
                (1234,null,null,"game", new ChessGame());
        gameDAO.createGame(gameData);
        gameDAO.clear();
        assertNotNull(gameDAO.listGames());
    }
    @Test
    public void updateSuccess () throws DataAccessException {
        GameData gameData = new GameData
                (1234,null,null,"game", new ChessGame());
        gameDAO.createGame(gameData);
        gameDAO.updateGame
                (new GameData(1234,"user",null,"game",new ChessGame()));
        GameData game = gameDAO.getGame(1234);
        assertNotNull(game.whiteUsername());
    }
    @Test
    public void updateFail () throws DataAccessException {
        GameData gameData = new GameData
                (1234,null,null,"game", new ChessGame());
        gameDAO.createGame(gameData);
        gameDAO.updateGame
                (new GameData(4321,"user",null,"game",new ChessGame()));
        GameData game = gameDAO.getGame(1234);
        assertNull(game.whiteUsername());
    }
    @Test
    public void clear() throws DataAccessException {
        GameData gameData = new GameData
                (1234,null,null,"game", new ChessGame());
        gameDAO.createGame(gameData);
        gameDAO.clear();
        assertNull(gameDAO.getGame(1234));
    }
}
