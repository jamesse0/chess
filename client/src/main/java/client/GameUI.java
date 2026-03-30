package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

import java.util.Objects;
import java.util.Scanner;

public class GameUI {
    private final ServerFacade server;
    private final Scanner scanner;
    private final Session session;

    public GameUI (ServerFacade server, Scanner scanner, Session session) {
        this.server = server;
        this.scanner = scanner;
        this.session = session;
    }

    public State run () {
        boolean isWhite = true;
        if (Objects.equals(session.getTeamColor(), "BLACK")){
            isWhite = false;
        }
        System.out.println("Here is the game, you are the " + session.getTeamColor() + " team.");
        drawBoard(new ChessGame().getBoard(), isWhite);
        System.out.printf("%nHere is the board (currently non-functional). Type 'leave' and then hit enter, " +
                "and then type and enter 'help' to return to Game Menu.%n");
        while (true) {
            System.out.printf("[IN_GAME] >>> %s", EscapeSequences.SET_TEXT_COLOR_GREEN);
            String line = scanner.nextLine();
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            var tokens = line.split(" ");
            if (tokens[0].trim().equals("leave")) {
                session.setGameID(null);
                session.setTeamColor(null);
                return State.loggedIN;
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
