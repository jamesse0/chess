package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class PreLoginUI {
    public PreLoginUI () {}

    public State run (Session session, ServerFacade server, Scanner scanner) {
        String line = scanner.nextLine();
        if (line.trim().isEmpty()) {
            System.out.println("Please enter a command.");
            return State.loggedOUT;
        }
        String[] tokens = line.split(" ");
        String command = tokens[0];
        return switch (command.toLowerCase()) {
            case "help" -> {helpStatement();
            yield State.loggedOUT;}
            case "register" ->
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

    private void handleRegister () {
        if
    }
}
