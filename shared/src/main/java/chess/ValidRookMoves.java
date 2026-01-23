package chess;
import java.util.*;
public class ValidRookMoves {

    public static List<ChessMove> validMoves(ChessPosition sPosition, ChessPiece thePiece, ChessBoard theBoard) {// returns ChessMove list of valid Rook moves
        List<ChessMove> listValidMoves = new ArrayList<>();
        int i = 1;
        while (sPosition.getRow()+i <= 8) {
            ChessPosition nPosition = new ChessPosition(sPosition.getRow()+i,sPosition.getColumn());
            ChessMove nMove = new ChessMove(sPosition, nPosition, null);
            if (theBoard.getPiece(nPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((nPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    listValidMoves.add(nMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                listValidMoves.add(nMove);
            }
            ++i;
        }
        i = 1;
        while (sPosition.getRow()-i >= 1) {
            ChessPosition nPosition = new ChessPosition(sPosition.getRow()-i,sPosition.getColumn());
            ChessMove nMove = new ChessMove(sPosition, nPosition, null);
            if (theBoard.getPiece(nPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((nPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    listValidMoves.add(nMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                listValidMoves.add(nMove);
            }
            ++i;
        }
        i = 1;
        while (sPosition.getColumn()-i >= 1) {
            ChessPosition nPosition = new ChessPosition(sPosition.getRow(),sPosition.getColumn()-i);
            ChessMove nMove = new ChessMove(sPosition, nPosition, null);
            if (theBoard.getPiece(nPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((nPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    listValidMoves.add(nMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                listValidMoves.add(nMove);
            }
            ++i;
        }
        i = 1;
        while (sPosition.getColumn()+i <= 8) {
            ChessPosition nPosition = new ChessPosition(sPosition.getRow(),sPosition.getColumn()+i);
            ChessMove nMove = new ChessMove(sPosition, nPosition, null);
            if (theBoard.getPiece(nPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((nPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    listValidMoves.add(nMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                listValidMoves.add(nMove);
            }
            ++i;
        }
        return listValidMoves;
    }
}
