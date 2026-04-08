package client;

import chess.*;
import ui.EscapeSequences;

import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class GameUI {
    private final ServerFacade server;
    private final Scanner scanner;
    private final UserSession userSession;
    private final WebSocketFacade ws;

    public GameUI (ServerFacade server, Scanner scanner, UserSession userSession, WebSocketFacade ws) {
        this.server = server;
        this.scanner = scanner;
        this.userSession = userSession;
        this.ws = ws;
    }

    public State run () {
        boolean isWhite = true;
        if (Objects.equals(userSession.getTeamColor(), "BLACK")){
            isWhite = false;
        }
        System.out.println("Here is the game, you are the " + userSession.getTeamColor() + " team.");
        drawBoard(userSession.getGame().getBoard(), isWhite);
        System.out.printf("%nHere is the board (currently non-functional). Type 'help' for options%n");
        while (true) {
            System.out.printf("[IN_GAME] >>> %s", EscapeSequences.SET_TEXT_COLOR_GREEN);
            String line = scanner.nextLine();
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            var tokens = line.split(" ");
            String command = tokens[0];
            switch (command.trim()) {
                case "leave" -> {
                    try {
                        ws.leave(userSession.getGameID(), userSession.getPlayerType(),
                                userSession.getUsername(), userSession.getTeamColor(), userSession.getAuthToken());
                        userSession.setGameID(null);
                        userSession.setTeamColor(null);
                        return State.loggedIN;
                    } catch (Exception e) {
                        System.out.println("There was an issue leaving. Please try again.");
                    }
                }
                case "help" -> {helpStatement();}
                case "redraw" -> {drawBoard(userSession.getGame().getBoard(), isWhite);}
                case "resign" -> {
                    try {
                        ws.resign(userSession.getGameID(), userSession.getPlayerType(), userSession.getUsername(),
                                userSession.getTeamColor(), userSession.getAuthToken());
                    } catch (Exception e) {
                        System.out.println("There was an issue resigning. Please try again.");
                    }
                }
                case "move" -> {
                    try {
                        move(tokens);
                        drawBoard(userSession.getGame().getBoard(), isWhite);
                    } catch (Exception e){
                        System.out.println("There was an issue making that move. Please try again.");
                    }
                }

                case "highlight" -> {}
            }
        }
    }

    private void helpStatement () {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "redraw"
                + EscapeSequences.RESET_TEXT_COLOR + "- to redraw the current board");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "highlight <row number><column letter>"
                + EscapeSequences.RESET_TEXT_COLOR + "- to show the potential moves of a piece at that space " +
                "(ex. highlight 2a)");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "move <row number><column letter> " +
                "<row number><column letter>"
                + EscapeSequences.RESET_TEXT_COLOR + "- to move a piece from one square to another (ex. move 2a 3a)");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "resign"
                + EscapeSequences.RESET_TEXT_COLOR + "- to forfeit the game");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "leave"
                + EscapeSequences.RESET_TEXT_COLOR + "- to leave the game and return to Game Menu");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help"
                + EscapeSequences.RESET_TEXT_COLOR + "- with possible commands");
    }

    private void highlight (String[] tokens, ChessBoard board, boolean isWhite) {

    }

    private void move (String[] tokens) throws Exception {
        if (tokens.length != 3) {
            System.out.println ("Incorrect Parameters Given. Please try again.");
        }
        else {
            String rawStart = tokens[1];
            String rawEnd = tokens[2];
            String[] sPosition = rawStart.split("");
            String[] ePosition = rawEnd.split("");
            if ((sPosition.length != 2) || (ePosition.length!=2)) {
                System.out.println ("Position coordinates not correct. Please try again.");
            }
            else {
                Map<String, Integer> columnConversion = Map.of(
                        "a", 1,
                        "b", 2,
                        "c", 3,
                        "d", 4,
                        "e", 5,
                        "f", 6,
                        "g", 7,
                        "h", 8
                );
                int startRow = Integer.parseInt(sPosition[0]);
                int startCol = columnConversion.get(sPosition[1]);
                int endRow = Integer.parseInt(ePosition[0]);
                int endCol = columnConversion.get(ePosition[1]);
                ChessPosition startPosition = new ChessPosition(startRow, startCol);
                ChessPosition endPosition = new ChessPosition(endRow, endCol);
                ChessMove chessMove = new ChessMove(startPosition,endPosition,null);
                ws.makeMove(userSession.getGameID(), userSession.getPlayerType(), userSession.getUsername(),
                        userSession.getTeamColor(), userSession.getAuthToken(), chessMove);
            }

        }
    }

    private void drawBoard(ChessBoard board, boolean isWhite) {
        String headerColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK;
        String reset = EscapeSequences.RESET_TEXT_COLOR+EscapeSequences.RESET_BG_COLOR;
        printHeader(isWhite);
        for (int r=0; r<8; r++) {
            int displayRow = isWhite ? (8 - r) : (r + 1);
            System.out.print(headerColor+ " "+displayRow+ " "+reset);
            for (int c = 0; c < 8; c++){
                int displayCol = isWhite ? c : (7 - c);
                boolean isLightSquare = (r + c) % 2 == 0;
                int boardRow = isWhite ? (8 - r) : (r + 1);
                int boardCol = isWhite ? (c + 1) : (8 - c);
                String bgColor = isLightSquare ? EscapeSequences.SET_BG_COLOR_WHITE:EscapeSequences.SET_BG_COLOR_BLACK;
                System.out.print(bgColor);
                ChessPiece piece = board.getPiece(new ChessPosition(boardRow,boardCol));
                String pieceChar;
                String pieceColor =isLightSquare?EscapeSequences.SET_BG_COLOR_WHITE:EscapeSequences.SET_BG_COLOR_BLACK;
                if (piece == null) {
                    pieceChar = " ";
                }
                else {
                    pieceColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                            EscapeSequences.SET_TEXT_COLOR_RED : EscapeSequences.SET_TEXT_COLOR_BLUE;
                    pieceChar = switch (piece.getPieceType()) {
                        case ChessPiece.PieceType.KING -> "K";
                        case ChessPiece.PieceType.QUEEN -> "Q";
                        case ChessPiece.PieceType.BISHOP -> "B";
                        case ChessPiece.PieceType.KNIGHT -> "N";
                        case ChessPiece.PieceType.ROOK -> "R";
                        case ChessPiece.PieceType.PAWN -> "P";
                    };
                }
                System.out.print(pieceColor+ " "+pieceChar+" ");
            }
            System.out.println(headerColor+ " "+displayRow+ " "+reset);
        }
        printHeader(isWhite);
    }

    private void printHeader(boolean isWhite) {
        String headerColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK;
        String[] header = {"   "," a "," b "," c "," d "," e "," f "," g "," h ","   "};
        if (isWhite) {
            System.out.print(headerColor);
            for (String letter: header) {
                System.out.print(letter);
            }
            System.out.println(EscapeSequences.RESET_TEXT_COLOR+EscapeSequences.RESET_BG_COLOR);
        }
        else {
            System.out.print(headerColor);
            for (int i = header.length - 1; i >= 0; i--) {
                System.out.print(header[i]);
            }
            System.out.println(EscapeSequences.RESET_TEXT_COLOR+EscapeSequences.RESET_BG_COLOR);
        }
    }


}
