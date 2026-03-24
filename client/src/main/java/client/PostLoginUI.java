package client;

import dataaccess.DataAccessException;
import service.CreateGameRequest;
import service.CreateGameResult;
import ui.EscapeSequences;

import java.util.Scanner;

public class PostLoginUI {
    private final ServerFacade server;
    private final Scanner scanner;
    private final Session session;

    public PostLoginUI (ServerFacade server, Scanner scanner, Session session) {
        this.server = server;
        this.scanner = scanner;
        this.session = session;
    }

    public State run () {
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) {
            System.out.println("Please enter a command.");
            return State.loggedOUT;
        }
        String[] tokens = line.split(" ");
        String command = tokens[0];
        return switch (command) {
          case "help" -> {
              helpStatement();
              yield State.loggedIN;
          }
          case "create" -> {
              try {
                  yield handleCreate(tokens);
              } catch (DataAccessException error) {
                  System.out.println("Sorry." + error.getMessage() + "Please try again.");
                  yield State.loggedIN;
              }
          }
          case "list" ->
        };
    }

    private void helpStatement () {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "create <NAME>"
                + EscapeSequences.RESET_TEXT_COLOR + "- to create a game");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "list"
                + EscapeSequences.RESET_TEXT_COLOR + "- shows all current games");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]"
                + EscapeSequences.RESET_TEXT_COLOR + "- to join a game");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "observe <ID>"
                + EscapeSequences.RESET_TEXT_COLOR + "- to spectate a game");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "logout"
                + EscapeSequences.RESET_TEXT_COLOR + "- to return to register/login menu");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "quit"
                + EscapeSequences.RESET_TEXT_COLOR + "- if you're done playing");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help"
                + EscapeSequences.RESET_TEXT_COLOR + "- with possible commands");
    }

    public State handleCreate (String[] tokens) throws DataAccessException {
        if (tokens.length != 2) {
            System.out.println ("Incorrect Parameters Given. Please try again.");
            return State.loggedIN;
        }
        else {
            CreateGameRequest request = new CreateGameRequest(tokens[1]);
            CreateGameResult result = server.createGame(request, session.getAuthToken());
            System.out.println("You create Game: " + tokens[1] + ". Type 'list' to find its information.");
            return State.loggedIN;
        }
    }
}
