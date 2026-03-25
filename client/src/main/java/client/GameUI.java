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
        drawBoard(new ChessGame().getBoard(), isWhite);
        System.out.println("Here is the board (currently non-functional). Type 'leave' to return to Game Menu.");
        while (true) {
            String line = scanner.nextLine();
            var tokens = line.split(" ");
            if (tokens[0].equals("leave")) {
                session.setGameID(null);
                session.setTeamColor(null);
                return State.loggedIN;
            }
        }
    }

    private void drawBoard(ChessBoard board, boolean isWhite) {
        String header_color = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK;
        String reset = EscapeSequences.RESET_TEXT_COLOR+EscapeSequences.RESET_BG_COLOR;
        printHeader(isWhite);
        for (int r=0; r<8; r++) {
            int displayRow = isWhite ? (8 - r) : (r + 1);
            System.out.print(header_color+ " "+displayRow+ " "+reset);
            for (int c = 0; c < 8; c++){
                int displayCol = isWhite ? c : (7 - c);
                boolean isLightSquare = (r + displayCol) % 2 == 0;
                String bgColor = isLightSquare ? EscapeSequences.SET_BG_COLOR_WHITE:EscapeSequences.SET_BG_COLOR_BLACK;
                System.out.print(bgColor);
                ChessPiece piece = board.getPiece(new ChessPosition(displayRow,displayCol+1));
                String pieceColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ?
                        EscapeSequences.SET_TEXT_COLOR_RED : EscapeSequences.SET_TEXT_COLOR_BLUE;
                String pieceChar = switch (piece.getPieceType()) {
                    case ChessPiece.PieceType.KING -> "K";
                    case ChessPiece.PieceType.QUEEN -> "Q";
                    case ChessPiece.PieceType.BISHOP -> "B";
                    case ChessPiece.PieceType.KNIGHT -> "N";
                    case ChessPiece.PieceType.ROOK -> "R";
                    case ChessPiece.PieceType.PAWN -> "P";
                };
                System.out.print(pieceColor+ " "+pieceChar+" ");
            }
            System.out.println(header_color+ " "+displayRow+ " "+reset);
        }
        printHeader(isWhite);
    }

    private void printHeader(boolean isWhite) {
        String header_color = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK;
        String[] header = {" a "," b "," c "," d "," e "," f "," g "," h "};
        if (isWhite) {
            System.out.print(header_color);
            for (String letter: header) {
                System.out.print(letter);
            }
            System.out.println(EscapeSequences.RESET_TEXT_COLOR+EscapeSequences.RESET_BG_COLOR);
        }
        else {
            System.out.print(header_color);
            for (int i = header.length - 1; i >= 0; i--) {
                System.out.print(header[i]);
            }
            System.out.println(EscapeSequences.RESET_TEXT_COLOR+EscapeSequences.RESET_BG_COLOR);
        }
    }
}
