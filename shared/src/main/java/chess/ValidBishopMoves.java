package chess;
import java.util.*;
public class ValidBishopMoves {

    public static List<ChessMove> validMoves(ChessPosition startPosition, ChessPiece thePiece, ChessBoard theBoard) { // returns a list of ChessMove that a bishop can make
        List<ChessMove> vMoves = new ArrayList<>();
        int i = 1;
        while ((startPosition.getRow()+i <= 8) && (startPosition.getColumn()+i <= 8)) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()+i,startPosition.getColumn()+i);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if (theBoard.getPiece(newPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((newPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    vMoves.add(newMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                vMoves.add(newMove);
            }
            ++i;
        }
        i = 1;
        while ((startPosition.getRow()+i <= 8) && (startPosition.getColumn()-i >= 1)) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()+i,startPosition.getColumn()-i);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if (theBoard.getPiece(newPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((newPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    vMoves.add(newMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                vMoves.add(newMove);
            }
            ++i;
        }
        i = 1;
        while ((startPosition.getRow()-i >= 1) && (startPosition.getColumn()+i <= 8)) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()-i,startPosition.getColumn()+i);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if (theBoard.getPiece(newPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((newPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    vMoves.add(newMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                vMoves.add(newMove);
            }
            ++i;
        }
        i=1;
        while ((startPosition.getRow()-i >= 1) && (startPosition.getColumn()-i >= 1)) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()-i,startPosition.getColumn()-i);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if (theBoard.getPiece(newPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((newPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    vMoves.add(newMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                vMoves.add(newMove);
            }
            ++i;
        }
        return vMoves;
    }
}
