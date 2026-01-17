package chess;
import java.util.*;
public class ValidKingMoves {
    public static List<ChessMove> validMoves (ChessPosition startPosition, ChessPiece thePiece, ChessBoard theBoard) {
        List<ChessMove> vMoves = new ArrayList<>();
        if (startPosition.getRow()+1 <=8) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() +1, startPosition.getColumn());
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if (startPosition.getRow()-1 >= 1) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() -1, startPosition.getColumn());
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if (startPosition.getColumn()+1 <=8) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn()+1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if (startPosition.getColumn()-1 >= 1) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn()-1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if ((startPosition.getColumn() + 1 <=8) && (startPosition.getRow() +1 <=8)) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if ((startPosition.getColumn() - 1 >= 1) && (startPosition.getRow() +1 <=8)) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if ((startPosition.getColumn() + 1 <=8) && (startPosition.getRow() -1 >= 1)) {
            ChessPosition newPosition = new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1);
            ChessMove newMove = new ChessMove(startPosition, newPosition, null);
            if ((theBoard.getPiece(newPosition) == null) ||
                    (theBoard.getPiece(newPosition).getTeamColor() != thePiece.getTeamColor())) {
                vMoves.add(newMove);
            }
        }
        if ((startPosition.getColumn() - 1 >= 1) && (startPosition.getRow() - 1 >= 1)) {
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
