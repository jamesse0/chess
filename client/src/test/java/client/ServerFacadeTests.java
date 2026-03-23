package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import service.*;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    private static ServerFacade facade;
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setup() throws Exception {
        facade.clear();
    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerPositive() throws Exception {
        var response = facade.register(new RegisterRequest("username", "password", "email"));
        assertNotNull(response);
    }

    @Test
    public void registerNegative() throws Exception {
        facade.register(new RegisterRequest("username", "password", "email"));
        assertThrows(Exception.class, () ->  facade.register
                (new RegisterRequest("username", "password", "email")));
    }

    @Test
    public void loginPositive () throws Exception {
        facade.register(new RegisterRequest("username", "password", "email"));
        var response = facade.login(new LoginRequest("username", "password"));
        assertNotNull(response);
    }

    @Test
    public void loginNegative () throws Exception {
        facade.register(new RegisterRequest("username", "password", "email"));
        assertThrows(Exception.class, () ->  facade.login
                (new LoginRequest("badusername", "badpassword")));
    }

    @Test
    public void logoutPositive () throws Exception {
        RegisterResult response = facade.register(new RegisterRequest("username", "password", "email"));
        assertDoesNotThrow(()-> facade.logout(new LogoutRequest(response.authToken())));
    }

    @Test
    public void logoutNegative () throws Exception {
        RegisterResult response = facade.register(new RegisterRequest("username", "password", "email"));
        assertThrows(Exception.class,()-> facade.logout(new LogoutRequest("badauth")));
    }

    @Test
    public void listPositive () throws Exception {
        var auth = facade.register(new RegisterRequest("username","password","email"));
        facade.createGame(new CreateGameRequest("game"), auth.authToken());
        var games = facade.listGames(new LogoutRequest(auth.authToken()));
        assertNotNull(games);
    }

    @Test
    public void listNegative () {
        assertThrows(Exception.class, () -> facade.listGames(new LogoutRequest("cookedAuth")));
    }

    @Test
    public void createPositive () throws Exception {
        var auth = facade.register(new RegisterRequest("username","password","email"));
        facade.createGame(new CreateGameRequest("game"), auth.authToken());
        var games = facade.listGames(new LogoutRequest(auth.authToken()));
        assertNotNull(games);
    }

    @Test
    public void createNegative () throws Exception {
        var auth = facade.register(new RegisterRequest("username","password","email"));
        assertThrows(Exception.class, ()->facade.createGame
                (new CreateGameRequest("game"), "badAuth"));
    }

    @Test
    public void joinPositive () throws Exception {
        var auth = facade.register(new RegisterRequest("username","password","email"));
        CreateGameResult result = facade.createGame(new CreateGameRequest("game"), auth.authToken());
        assertDoesNotThrow(() -> facade.joinGame(new JoinGameRequest
                ("WHITE", result.gameID()), auth.authToken()));
    }

    @Test
    public void joinNegative () throws Exception {
        var auth = facade.register(new RegisterRequest("username","password","email"));
        CreateGameResult result = facade.createGame(new CreateGameRequest("game"), auth.authToken());
        assertThrows(Exception.class,() -> facade.joinGame(new JoinGameRequest
                ("WHITE", result.gameID()), "badAuth"));
    }
}
