package client;

import java.util.Scanner;

public class ChessClient {
    private String authToken;
    private String username;
    private State currState;
    private final ServerFacade server;

    public ChessClient (String serverURL) {
        server = new ServerFacade(serverURL);
        currState = State.loggedOUT;
    }

    public void run () {
        System.out.println("Welcome to cs240 Chess! type 'help' to start.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (currState != State.QUIT) {
            currState = switch (currState) {
                case State.loggedOUT ->
            };
        }
    }


}
