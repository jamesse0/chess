package client;

import dataaccess.DataAccessException;
import model.GameData;
import service.*;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Scanner;

public class PostLoginUI {
    private final ServerFacade server;
    private final Scanner scanner;
    private final Session session;
    private ArrayList<GameData> userGames;

    public PostLoginUI (ServerFacade server, Scanner scanner, Session session) {
        this.server = server;
        this.scanner = scanner;
        this.session = session;
        userGames = new ArrayList<>();
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
              } catch (Exception error) {
                  System.out.println
                          ("Sorry. There was an issue creating your game. Check that your inputs are correct. Please try again.");
                  yield State.loggedIN;
              }
          }
          case "list" -> {
              try {
                  yield handleList(tokens);
              } catch (Exception error) {
                  System.out.println("Sorry. There was an issue listing all the games. Please try again.");
                  yield State.loggedIN;
              }
          }
          case "join" -> {
              try {
                  yield handleJoin(tokens);
              } catch (Exception error) {
                  System.out.println("Sorry. There was an issue joining this game. " +
                          "Make sure that the team you're joining has not already been taken.Please try again.");
                  yield State.loggedIN;
              }
          }
          case "observe" -> handleObserve(tokens);
          case "logout" -> {
              try {
                  System.out.println
                          ("Logging you out and returning you to Login/Register Menu. Type 'help' for options.");
                  yield handleLogout(tokens);
              } catch (Exception error) {
                  System.out.println("Sorry. There was an issue logging out. Please try again.");
                  yield State.loggedIN;
              }
          }
          case "quit" -> {
              try {
                  handleLogout(tokens);
                  System.out.println("Goodbye. See you soon.");
                  yield State.QUIT;
              } catch (Exception error) {
                  System.out.println("Sorry. There was an issue quiting. Please try again.");
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
            userGames = (ArrayList<GameData>) result.games();
            if (userGames.isEmpty()) {
                System.out.println("There are currently no games.");
            }
            for (int i = 0; i < userGames.size(); i++) {
                String yellow = EscapeSequences.SET_TEXT_COLOR_YELLOW;
                String normal = EscapeSequences.RESET_TEXT_COLOR;
                System.out.printf("%sGame Name: %s%s %sWHITE user: %s%s %sBLACK user: %s%s %sID: %s%d%s%n", yellow,
                        normal, userGames.get(i).gameName(),yellow, normal, userGames.get(i).whiteUsername(),
                        yellow, normal, userGames.get(i).blackUsername(), yellow, normal, i+1,normal);
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
            int gameID;
            try {
                gameID = userGames.get(Integer.parseInt(tokens[1]) - 1).gameID();
            } catch (Exception error) {
                System.out.println("Enter the correct ID as a digit.");
                return State.loggedIN;
            }
            JoinGameRequest request = new JoinGameRequest(tokens[2].toUpperCase(), gameID);
            server.joinGame(request, session.getAuthToken());
            session.setGameID(request.gameID());
            session.setTeamColor(request.playerColor());
            String color = EscapeSequences.SET_TEXT_COLOR_MAGENTA;
            String normal = EscapeSequences.RESET_TEXT_COLOR;
            System.out.printf("Joining game %s%s%s as %s%s%s%n",
                    color, tokens[1], normal, color, request.playerColor(), normal);
            return State.inGAME;
        }
    }

    public State handleObserve (String[] tokens) {
        if (tokens.length != 2) {
            System.out.println ("Incorrect Parameters Given. Please try again.");
            return State.loggedIN;
        }
        else {
            int gameID;
            try {
                gameID = userGames.get(Integer.parseInt(tokens[1]) - 1).gameID();
            } catch (Exception error) {
                System.out.println("Enter the correct ID as a digit.");
                return State.loggedIN;
            }
            session.setGameID(gameID);
            String color = EscapeSequences.SET_TEXT_COLOR_MAGENTA;
            String normal = EscapeSequences.RESET_TEXT_COLOR;
            System.out.printf("Joining game %s%s%s%n",
                    color, tokens[1], normal);
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
            session.setUsername(null);
            return State.loggedOUT;
        }
    }
}
