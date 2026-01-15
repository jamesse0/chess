package chess;
import java.util.*;
public class ValidKnightMoves {

    public static List<ChessMove> validMoves (ChessPosition startPosition, ChessPiece thePiece, ChessBoard theBoard) {
        List<ChessMove> vMoves = new ArrayList<>();
        if (startPosition.getRow()+2 <= 8) {
            ChessPosition newLeftPosition = new ChessPosition(startPosition.getRow()+2,startPosition.getColumn()-1);
            ChessMove newLeftMove = new ChessMove(startPosition, newLeftPosition, null);
            if (newLeftPosition.getColumn() >= 1) {
                if ((theBoard.getPiece(newLeftPosition) == null) ||
                        (theBoard.getPiece(newLeftPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newLeftMove);
                }
            }
            ChessPosition newRightPosition = new ChessPosition(startPosition.getRow()+2,startPosition.getColumn()+1);
            ChessMove newRightMove = new ChessMove(startPosition, newRightPosition, null);
            if (newRightPosition.getColumn() <=8) {
                if ((theBoard.getPiece(newRightPosition) == null) ||
                        (theBoard.getPiece(newRightPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newRightMove);
                }
            }
        }
        if (startPosition.getRow()-2 >= 1) {
            ChessPosition newLeftPosition = new ChessPosition(startPosition.getRow()-2,startPosition.getColumn()-1);
            ChessMove newLeftMove = new ChessMove(startPosition, newLeftPosition, null);
            if (newLeftPosition.getColumn() >= 1) {
                if ((theBoard.getPiece(newLeftPosition) == null) ||
                        (theBoard.getPiece(newLeftPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newLeftMove);
                }
            }
            ChessPosition newRightPosition = new ChessPosition(startPosition.getRow()-2,startPosition.getColumn()+1);
            ChessMove newRightMove = new ChessMove(startPosition, newRightPosition, null);
            if (newRightPosition.getColumn() <=8) {
                if ((theBoard.getPiece(newRightPosition) == null) ||
                        (theBoard.getPiece(newRightPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newRightMove);
                }
            }
        }
        if (startPosition.getColumn()+2 <= 8) {
            ChessPosition newUpPosition = new ChessPosition(startPosition.getRow()+1,startPosition.getColumn()+2);
            ChessMove newUpMove = new ChessMove(startPosition, newUpPosition, null);
            if (newUpPosition.getColumn() >= 1) {
                if ((theBoard.getPiece(newUpPosition) == null) ||
                    (theBoard.getPiece(newUpPosition).getTeamColor() != thePiece.getTeamColor())){
                    vMoves.add(newUpMove);
                }
            }
            ChessPosition newDownPosition = new ChessPosition(startPosition.getRow()-1,startPosition.getColumn()+2);
            ChessMove newDownMove = new ChessMove(startPosition, newDownPosition, null);
            if (newDownPosition.getColumn() <=8) {
                if ((theBoard.getPiece(newDownPosition) == null) ||
                    (theBoard.getPiece(newDownPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newDownMove);
                }
            }
        }
        if (startPosition.getColumn()-2 >= 1) {
            ChessPosition newUpPosition = new ChessPosition(startPosition.getRow()+1,startPosition.getColumn()-2);
            ChessMove newUpMove = new ChessMove(startPosition, newUpPosition, null);
            if (newUpPosition.getColumn() >= 1) {
                if ((theBoard.getPiece(newUpPosition) == null) ||
                        (theBoard.getPiece(newUpPosition).getTeamColor() != thePiece.getTeamColor())){
                    vMoves.add(newUpMove);
                }
            }
            ChessPosition newDownPosition = new ChessPosition(startPosition.getRow()-1,startPosition.getColumn()-2);
            ChessMove newDownMove = new ChessMove(startPosition, newDownPosition, null);
            if (newDownPosition.getColumn() <=8) {
                if ((theBoard.getPiece(newDownPosition) == null) ||
                        (theBoard.getPiece(newDownPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newDownMove);
                }
            }
        }
        return vMoves;
    }
}
