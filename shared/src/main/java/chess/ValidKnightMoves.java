package chess;
import java.util.*;
public class ValidKnightMoves {

    public static List<ChessMove> validMoves(ChessPosition sPosition, ChessPiece thePiece, ChessBoard theBoard) { //returns a ChessMove list of valid knight moves
        List<ChessMove> listValidMoves = new ArrayList<>();
        if (sPosition.getRow() + 2 <= 8) {
            if (sPosition.getColumn() - 1 >= 1) {
                ChessPosition nLP = new ChessPosition(sPosition.getRow() + 2, sPosition.getColumn() - 1);
                ChessMove nLM = new ChessMove(sPosition, nLP, null);
                if ((theBoard.getPiece(nLP) == null) ||
                        (theBoard.getPiece(nLP).getTeamColor() != thePiece.getTeamColor())) {
                    listValidMoves.add(nLM);
                }
            }
            if (sPosition.getColumn() + 1 <= 8) {
                ChessPosition nRP = new ChessPosition(sPosition.getRow() + 2, sPosition.getColumn() + 1);
                ChessMove nRM = new ChessMove(sPosition, nRP, null);
                if ((theBoard.getPiece(nRP) == null) ||
                        (theBoard.getPiece(nRP).getTeamColor() != thePiece.getTeamColor())) {
                    listValidMoves.add(nRM);
                }
            }
        }

        if (sPosition.getRow() - 2 >= 1) {
            if (sPosition.getColumn() - 1 >= 1) {
                ChessPosition nLP = new ChessPosition(sPosition.getRow() - 2, sPosition.getColumn() - 1);
                ChessMove nLM = new ChessMove(sPosition, nLP, null);
                if ((theBoard.getPiece(nLP) == null) ||
                        (theBoard.getPiece(nLP).getTeamColor() != thePiece.getTeamColor())) {
                    listValidMoves.add(nLM);
                }
            }
            if (sPosition.getColumn() + 1 <= 8) {
                ChessPosition nRP = new ChessPosition(sPosition.getRow() - 2, sPosition.getColumn() + 1);
                ChessMove nRM = new ChessMove(sPosition, nRP, null);
                if ((theBoard.getPiece(nRP) == null) ||
                        (theBoard.getPiece(nRP).getTeamColor() != thePiece.getTeamColor())) {
                    listValidMoves.add(nRM);
                }
            }
        }
        if (sPosition.getColumn() + 2 <= 8) {
            if (sPosition.getRow() + 1 <= 8) {
                ChessPosition nUP = new ChessPosition(sPosition.getRow() + 1, sPosition.getColumn() + 2);
                ChessMove nUM = new ChessMove(sPosition, nUP, null);
                if ((theBoard.getPiece(nUP) == null) ||
                        (theBoard.getPiece(nUP).getTeamColor() != thePiece.getTeamColor())) {
                    listValidMoves.add(nUM);
                }
            }
            if (sPosition.getRow() - 1 >= 1) {
                ChessPosition nDP = new ChessPosition(sPosition.getRow() - 1, sPosition.getColumn() + 2);
                ChessMove nDM = new ChessMove(sPosition, nDP, null);
                if ((theBoard.getPiece(nDP) == null) ||
                        (theBoard.getPiece(nDP).getTeamColor() != thePiece.getTeamColor())) {
                    listValidMoves.add(nDM);
                }
            }
        }
        if (sPosition.getColumn() - 2 >= 1) {
            if (sPosition.getRow() + 1 <= 8) {
                ChessPosition nUP = new ChessPosition(sPosition.getRow() + 1, sPosition.getColumn() - 2);
                ChessMove nUM = new ChessMove(sPosition, nUP, null);
                if ((theBoard.getPiece(nUP) == null) ||
                        (theBoard.getPiece(nUP).getTeamColor() != thePiece.getTeamColor())) {
                    listValidMoves.add(nUM);
                }
            }
            if (sPosition.getRow() - 1 >= 1) {
                ChessPosition nDP = new ChessPosition(sPosition.getRow() - 1, sPosition.getColumn() - 2);
                ChessMove nDM = new ChessMove(sPosition, nDP, null);
                if ((theBoard.getPiece(nDP) == null) ||
                        (theBoard.getPiece(nDP).getTeamColor() != thePiece.getTeamColor())) {
                    listValidMoves.add(nDM);
                }
            }
        }
        return listValidMoves;
    }
}
