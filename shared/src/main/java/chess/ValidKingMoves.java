package chess;
import java.util.*;
public class ValidKingMoves {
    public static List<ChessMove> validMoves (ChessPosition startPosition, ChessPiece thePiece, ChessBoard theBoard) { //returns a list of ChessMoves that a King can make
        List<ChessMove> vMoves = new ArrayList<>();
        if (startPosition.getRow()+1 <=8) { //checks the position right in front of the king
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() +1, startPosition.getColumn());
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if (startPosition.getRow()-1 >= 1) { //checks the position behind the king
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() -1, startPosition.getColumn());
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if (startPosition.getColumn()+1 <=8) { //checks position right of the king
            ChessPosition newPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn()+1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if (startPosition.getColumn()-1 >= 1) { //checks position left of king
            ChessPosition newPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn()-1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if ((startPosition.getColumn() + 1 <=8) && (startPosition.getRow() +1 <=8)) { //checks right up diagonal
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if ((startPosition.getColumn() - 1 >= 1) && (startPosition.getRow() +1 <=8)) { // checks left up diagonal
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if ((startPosition.getColumn() + 1 <=8) && (startPosition.getRow() -1 >= 1)) { // checks right down diagonal
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if ((startPosition.getColumn() - 1 >= 1) && (startPosition.getRow() - 1 >= 1)) { // checks left down diagonal
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        return vMoves;
    }
}
