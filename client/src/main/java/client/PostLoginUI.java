package client;

import dataaccess.DataAccessException;
import model.GameData;
import service.*;
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
        return switch (command.trim()) {
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
          case "list" -> {
              try {
                  yield handleList(tokens);
              } catch (DataAccessException error) {
                  System.out.println("Sorry." + error.getMessage() + "Please try again.");
                  yield State.loggedIN;
              }
          }
          case "join" -> {
              try {
                  yield handleJoin(tokens);
              } catch (DataAccessException error) {
                  System.out.println("Sorry." + error.getMessage() + "Please try again.");
                  yield State.loggedIN;
              }
          }
          case "observe" -> handleObserve(tokens);
          case "logout" -> {
              try {
                  yield handleLogout(tokens);
              } catch (DataAccessException error) {
                  System.out.println("Sorry." + error.getMessage() + "Please try again.");
                  yield State.loggedIN;
              }
          }
          case "quit" -> {
              try {
                  handleLogout(tokens);
                  yield State.QUIT;
              } catch (DataAccessException error) {
                  System.out.println("Sorry." + error.getMessage() + "Please try again.");
                  yield State.loggedIN;
              }
          }
          default -> {
              System.out.println("Unknown command: " + command);
              System.out.println("Type 'help' to see valid commands.");
              yield State.loggedIN;
          }
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

    public State handleList (String[] tokens) throws DataAccessException {
        if (tokens.length != 1) {
            System.out.println ("Incorrect Parameters Given. Please try again.");
            return State.loggedIN;
        }
        else {
            LogoutRequest request = new LogoutRequest(session.getAuthToken());
            ListGamesResult result = server.listGames(request);
            for (GameData game: result.games()) {
                String yellow = EscapeSequences.SET_TEXT_COLOR_YELLOW;
                String normal = EscapeSequences.RESET_TEXT_COLOR;
                System.out.printf("%sGame Name: %s%s %sWHITE user: %s%s %sBLACK user: %s%s %sID: %s%d%s%n",
                        yellow, normal, game.gameName(),yellow, normal, game.whiteUsername(),
                                yellow, normal, game.blackUsername(),yellow, normal, game.gameID(),normal);
            }
            return State.loggedIN;
        }
    }

    public State handleJoin (String[] tokens) throws DataAccessException {
        if (tokens.length != 3) {
            System.out.println ("Incorrect Parameters Given. Please try again.");
            return State.loggedIN;
        }
        else {
            JoinGameRequest request = new JoinGameRequest(tokens[2], Integer.parseInt(tokens[1]));
            server.joinGame(request, session.getAuthToken());
            session.setGameID(request.gameID());
            session.setTeamColor(request.playerColor());
            String color = EscapeSequences.SET_TEXT_COLOR_MAGENTA;
            String normal = EscapeSequences.RESET_TEXT_COLOR;
            System.out.printf("Joining game %s%d%s as %s%s%s%n",
                    color, request.gameID(), normal, color, request.playerColor(), normal);
            return State.inGAME;
        }
    }

    public State handleObserve (String[] tokens) {
        if (tokens.length != 2) {
            System.out.println ("Incorrect Parameters Given. Please try again.");
            return State.loggedIN;
        }
        else {
            session.setGameID(Integer.parseInt(tokens[1]));
            String color = EscapeSequences.SET_TEXT_COLOR_MAGENTA;
            String normal = EscapeSequences.RESET_TEXT_COLOR;
            System.out.printf("Joining game %s%d%s%n",
                    color, session.getGameID(), normal);
            return State.inGAME;
        }
    }

    public State handleLogout (String[] tokens) throws DataAccessException {
        if (tokens.length != 1) {
            System.out.println ("Incorrect Parameters Given. Please try again.");
            return State.loggedIN;
        }
        else {
            LogoutRequest request = new LogoutRequest(session.getAuthToken());
            server.logout(request);
            session.setAuth(null);
            return State.loggedOUT;
        }
    }
}
