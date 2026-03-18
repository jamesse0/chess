package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import service.LoginRequest;
import service.RegisterRequest;

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
}
