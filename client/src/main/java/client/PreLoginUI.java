package client;

import dataaccess.DataAccessException;
import service.LoginRequest;
import service.RegisterRequest;
import service.RegisterResult;
import ui.EscapeSequences;

import java.util.Scanner;

public class PreLoginUI {
    private final ServerFacade server;
    private final Scanner scanner;
    private final Session session;

    public PreLoginUI (ServerFacade server, Scanner scanner, Session session) {
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
        return switch (command.toLowerCase()) {
            case "help" -> {
                helpStatement();
                yield State.loggedOUT;
            }
            case "register" -> {
                try {
                    yield handleRegister(tokens);
                } catch (Exception error) {
                    System.out.println
                            ("Sorry. We could not register you. That username may already be taken. Please try again.");
                    yield State.loggedOUT;
                }
            }
            case "login" -> {
                try {
                    yield handleLogin(tokens);
                } catch (Exception error) {
                    System.out.println("Sorry. We could not log you in. Have you already registered? If so, " +
                            "ensure your username and password are correct. Please try again.");
                    yield State.loggedOUT;
                }
            }
            case "quit" -> {
                System.out.println("Goodbye. See you soon.");
                session.setAuth(null);
                session.setUsername(null);
                yield State.QUIT;
            }
            default -> {
                System.out.println("Unknown command: " + command);
                System.out.println("Type 'help' to see valid commands.");
                yield State.loggedOUT;
            }
        };
    }

    private void helpStatement () {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>"
                + EscapeSequences.RESET_TEXT_COLOR + "- to create an account");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>"
                + EscapeSequences.RESET_TEXT_COLOR + "- to play chess");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "quit"
                + EscapeSequences.RESET_TEXT_COLOR + "- to stop playing chess (ends program)");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help"
                + EscapeSequences.RESET_TEXT_COLOR + "- with possible commands");
    }

    private State handleRegister (String [] tokens) throws DataAccessException {
        if (tokens.length != 4) {
            System.out.println ("Incorrect Parameters Given. Please try again.");
            return State.loggedOUT;
        }
        else {
            RegisterRequest request = new RegisterRequest(tokens[1],tokens[2], tokens[3]);
            RegisterResult result = server.register(request);
            session.setAuth(result.authToken());
            session.setUsername(result.username());
            System.out.println("Registered and logged in as " + result.username() + ". Type 'help' for new options.");
            return State.loggedIN;
        }
    }

    private State handleLogin (String[] tokens) throws DataAccessException {
        if (tokens.length != 3) {
            System.out.println ("Incorrect Parameters Given. Please try again.");
            return State.loggedOUT;
        }
        else {
            LoginRequest request = new LoginRequest(tokens[1],tokens[2]);
            RegisterResult result = server.login(request);
            session.setUsername(result.username());
            session.setAuth(result.authToken());
            System.out.println("Logged in as " + result.username() + ". Type 'help' for new options.");
            return State.loggedIN;
        }
    }
}
