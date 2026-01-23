package chess;
import java.util.*;
public class ValidBishopMoves {

    public static List<ChessMove> validMoves(ChessPosition sPos, ChessPiece thePiece, ChessBoard theBoard) { // returns a list of ChessMove that a bishop can make
        List<ChessMove> listVMoves = new ArrayList<>();
        int i = 1;
        while ((sPos.getRow()+i <= 8) && (sPos.getColumn()+i <= 8)) {
            ChessPosition nPosition = new ChessPosition(sPos.getRow()+i,sPos.getColumn()+i);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if (theBoard.getPiece(nPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((nPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    listVMoves.add(nMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                listVMoves.add(nMove);
            }
            ++i;
        }
        i = 1;
        while ((sPos.getRow()+i <= 8) && (sPos.getColumn()-i >= 1)) {
            ChessPosition nPosition = new ChessPosition(sPos.getRow()+i,sPos.getColumn()-i);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if (theBoard.getPiece(nPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((nPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    listVMoves.add(nMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                listVMoves.add(nMove);
            }
            ++i;
        }
        i = 1;
        while ((sPos.getRow()-i >= 1) && (sPos.getColumn()+i <= 8)) {
            ChessPosition nPosition = new ChessPosition(sPos.getRow()-i,sPos.getColumn()+i);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if (theBoard.getPiece(nPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((nPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    listVMoves.add(nMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                listVMoves.add(nMove);
            }
            ++i;
        }
        i=1;
        while ((sPos.getRow()-i >= 1) && (sPos.getColumn()-i >= 1)) {
            ChessPosition nPosition = new ChessPosition(sPos.getRow()-i,sPos.getColumn()-i);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if (theBoard.getPiece(nPosition) != null) {
                ChessPiece otherPiece = theBoard.getPiece((nPosition));
                if (otherPiece.getTeamColor() != thePiece.getTeamColor()) {
                    listVMoves.add(nMove);
                    break;
                }
                else {
                    break;
                }
            }
            else {
                listVMoves.add(nMove);
            }
            ++i;
        }
        return listVMoves;
    }
}
