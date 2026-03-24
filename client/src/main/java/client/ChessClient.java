package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class ChessClient {
    private Session session;
    private State currState;
    private final ServerFacade server;

    public ChessClient (String serverURL) {
        server = new ServerFacade(serverURL);
        currState = State.loggedOUT;
        session = new Session();
    }

    public void run () {
        System.out.println("Welcome to cs240 Chess! type 'help' to start.");
        Scanner scanner = new Scanner(System.in);
        while (currState != State.QUIT) {
            System.out.print(prompt());
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            currState = switch (currState) {
                case State.loggedOUT ->
            };
        }
    }

    public String prompt () {
        String state = switch (currState) {
            case State.loggedOUT -> "LOGGED_OUT";
            case State.loggedIN -> "LOGGED_IN";
            case inGAME -> "IN_GAME";
            case QUIT -> "QUIT";
        };
        return String.format("[%s] >>> %s",state, EscapeSequences.SET_TEXT_COLOR_GREEN);
    }


}
