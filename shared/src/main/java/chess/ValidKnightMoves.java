package chess;
import java.util.*;
public class ValidKnightMoves {

    public static List<ChessMove> validMoves(ChessPosition startPosition, ChessPiece thePiece, ChessBoard theBoard) { //returns a ChessMove list of valid knight moves
        List<ChessMove> vMoves = new ArrayList<>();
        if (startPosition.getRow() + 2 <= 8) {
            if (startPosition.getColumn() - 1 >= 1) {
                ChessPosition newLeftPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() - 1);
                ChessMove newLeftMove = new ChessMove(startPosition, newLeftPosition, null);
                if ((theBoard.getPiece(newLeftPosition) == null) ||
                        (theBoard.getPiece(newLeftPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newLeftMove);
                }
            }
            if (startPosition.getColumn() + 1 <= 8) {
                ChessPosition newRightPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() + 1);
                ChessMove newRightMove = new ChessMove(startPosition, newRightPosition, null);
                if ((theBoard.getPiece(newRightPosition) == null) ||
                        (theBoard.getPiece(newRightPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newRightMove);
                }
            }
        }

        if (startPosition.getRow() - 2 >= 1) {
            if (startPosition.getColumn() - 1 >= 1) {
                ChessPosition newLeftPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1);
                ChessMove newLeftMove = new ChessMove(startPosition, newLeftPosition, null);
                if ((theBoard.getPiece(newLeftPosition) == null) ||
                        (theBoard.getPiece(newLeftPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newLeftMove);
                }
            }
            if (startPosition.getColumn() + 1 <= 8) {
                ChessPosition newRightPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1);
                ChessMove newRightMove = new ChessMove(startPosition, newRightPosition, null);
                if ((theBoard.getPiece(newRightPosition) == null) ||
                        (theBoard.getPiece(newRightPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newRightMove);
                }
            }
        }
        if (startPosition.getColumn() + 2 <= 8) {
            if (startPosition.getRow() + 1 <= 8) {
                ChessPosition newUpPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 2);
                ChessMove newUpMove = new ChessMove(startPosition, newUpPosition, null);
                if ((theBoard.getPiece(newUpPosition) == null) ||
                        (theBoard.getPiece(newUpPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newUpMove);
                }
            }
            if (startPosition.getRow() - 1 >= 1) {
                ChessPosition newDownPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 2);
                ChessMove newDownMove = new ChessMove(startPosition, newDownPosition, null);
                if ((theBoard.getPiece(newDownPosition) == null) ||
                        (theBoard.getPiece(newDownPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newDownMove);
                }
            }
        }
        if (startPosition.getColumn() - 2 >= 1) {
            if (startPosition.getRow() + 1 <= 8) {
                ChessPosition newUpPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 2);
                ChessMove newUpMove = new ChessMove(startPosition, newUpPosition, null);
                if ((theBoard.getPiece(newUpPosition) == null) ||
                        (theBoard.getPiece(newUpPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newUpMove);
                }
            }
            if (startPosition.getRow() - 1 >= 1) {
                ChessPosition newDownPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 2);
                ChessMove newDownMove = new ChessMove(startPosition, newDownPosition, null);
                if ((theBoard.getPiece(newDownPosition) == null) ||
                        (theBoard.getPiece(newDownPosition).getTeamColor() != thePiece.getTeamColor())) {
                    vMoves.add(newDownMove);
                }
            }
        }
        return vMoves;
    }
}
