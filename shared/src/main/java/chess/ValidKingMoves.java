package chess;
import java.util.*;
public class ValidKingMoves {
    public static List<ChessMove> validMoves (ChessPosition sPos, ChessPiece thePiece, ChessBoard theBoard) { //returns a list of ChessMoves that a King can make
        List<ChessMove> listVMoves = new ArrayList<>();
        if (sPos.getRow()+1 <=8) { //checks the position right in front of the king
            ChessPosition nPosition = new ChessPosition(sPos.getRow() +1, sPos.getColumn());
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if ((theBoard.getPiece(nPosition) == null) ||
                    (theBoard.getPiece(nPosition).getTeamColor() != thePiece.getTeamColor())) {
                listVMoves.add(nMove);
            }
        }
        if (sPos.getRow()-1 >= 1) { //checks the position behind the king
            ChessPosition nPosition = new ChessPosition(sPos.getRow() -1, sPos.getColumn());
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if ((theBoard.getPiece(nPosition) == null) ||
                    (theBoard.getPiece(nPosition).getTeamColor() != thePiece.getTeamColor())) {
                listVMoves.add(nMove);
            }
        }
        if (sPos.getColumn()+1 <=8) { //checks position right of the king
            ChessPosition nPosition = new ChessPosition(sPos.getRow(), sPos.getColumn()+1);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if ((theBoard.getPiece(nPosition) == null) ||
                    (theBoard.getPiece(nPosition).getTeamColor() != thePiece.getTeamColor())) {
                listVMoves.add(nMove);
            }
        }
        if (sPos.getColumn()-1 >= 1) { //checks position left of king
            ChessPosition nPosition = new ChessPosition(sPos.getRow(), sPos.getColumn()-1);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if ((theBoard.getPiece(nPosition) == null) ||
                    (theBoard.getPiece(nPosition).getTeamColor() != thePiece.getTeamColor())) {
                listVMoves.add(nMove);
            }
        }
        if ((sPos.getColumn() + 1 <=8) && (sPos.getRow() +1 <=8)) { //checks right up diagonal
            ChessPosition nPosition = new ChessPosition(sPos.getRow()+1, sPos.getColumn()+1);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if ((theBoard.getPiece(nPosition) == null) ||
                    (theBoard.getPiece(nPosition).getTeamColor() != thePiece.getTeamColor())) {
                listVMoves.add(nMove);
            }
        }
        if ((sPos.getColumn() - 1 >= 1) && (sPos.getRow() +1 <=8)) { // checks left up diagonal
            ChessPosition nPosition = new ChessPosition(sPos.getRow()+1, sPos.getColumn()-1);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if ((theBoard.getPiece(nPosition) == null) ||
                    (theBoard.getPiece(nPosition).getTeamColor() != thePiece.getTeamColor())) {
                listVMoves.add(nMove);
            }
        }
        if ((sPos.getColumn() + 1 <=8) && (sPos.getRow() -1 >= 1)) { // checks right down diagonal
            ChessPosition nPosition = new ChessPosition(sPos.getRow()-1, sPos.getColumn()+1);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if ((theBoard.getPiece(nPosition) == null) ||
                    (theBoard.getPiece(nPosition).getTeamColor() != thePiece.getTeamColor())) {
                listVMoves.add(nMove);
            }
        }
        if ((sPos.getColumn() - 1 >= 1) && (sPos.getRow() - 1 >= 1)) { // checks left down diagonal
            ChessPosition nPosition = new ChessPosition(sPos.getRow()-1, sPos.getColumn()-1);
            ChessMove nMove = new ChessMove(sPos, nPosition, null);
            if ((theBoard.getPiece(nPosition) == null) ||
                    (theBoard.getPiece(nPosition).getTeamColor() != thePiece.getTeamColor())) {
                listVMoves.add(nMove);
            }
        }
        return listVMoves;
    }
}
