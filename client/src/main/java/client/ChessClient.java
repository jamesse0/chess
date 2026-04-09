package client;

import ui.EscapeSequences;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class ChessClient implements NotificationHandler{
    private UserSession userSession;
    private State currState;
    private final ServerFacade server;
    private final WebSocketFacade ws;
    private final PreLoginUI preLoginUI;
    private final PostLoginUI postLoginUI;
    private final GameUI gameUI;
    private final Scanner scanner;
    public ChessClient (String serverURL) throws Exception {

        server = new ServerFacade(serverURL);
        ws = new WebSocketFacade(serverURL, this);
        currState = State.loggedOUT;
        userSession = new UserSession();
        scanner = new Scanner(System.in);
        preLoginUI = new PreLoginUI(server, scanner, userSession,ws);
        postLoginUI = new PostLoginUI(server, scanner, userSession, ws);
        gameUI = new GameUI(server, scanner, userSession,ws);
    }

    public void run () {
        System.out.println("Welcome to cs240 Chess! type 'help' to start.");
        while (currState != State.QUIT) {
            System.out.print(prompt());
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            currState = switch (currState) {
                case State.loggedOUT -> preLoginUI.run();
                case State.loggedIN -> postLoginUI.run();
                case State.inGAME -> gameUI.run();
                default -> State.loggedOUT;
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
        return String.format("%s[%s] >>> %s",EscapeSequences.RESET_TEXT_COLOR,state,
                EscapeSequences.SET_TEXT_COLOR_GREEN);
    }


    @Override
    public void notify(ServerMessage serverMessage) {
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGameMessage load = (LoadGameMessage) serverMessage;
            userSession.setGame(load.getGame());
            gameUI.drawBoard(userSession.getGame().getBoard(),userSession.getTeamColor().equals("WHITE"),
                    false, null);
            System.out.print(prompt());
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage notification = (NotificationMessage) serverMessage;
            String message = notification.getMessage();
            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + message + EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(prompt());
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            ErrorMessage error = (ErrorMessage) serverMessage;
            String errorMessage = error.getErrorMessage();
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage + EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(prompt());
        }
    }
}
