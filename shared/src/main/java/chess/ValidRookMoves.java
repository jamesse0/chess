package chess;
import java.util.*;
public class ValidRookMoves {

    public static List<ChessMove> validMoves(ChessPosition sPosition, ChessPiece thePiece, ChessBoard theBoard) {
        List<ChessMove> listValidMoves = new ArrayList<>();
        getDirection(listValidMoves,sPosition,thePiece,theBoard,1,0);
        getDirection(listValidMoves,sPosition,thePiece,theBoard,-1,0);
        getDirection(listValidMoves,sPosition,thePiece,theBoard,0,-1);
        getDirection(listValidMoves,sPosition,thePiece,theBoard,0,1);
        return listValidMoves;
    }

    public static void getDirection(List<ChessMove> listValidMoves, ChessPosition sPosition,
                                    ChessPiece thePiece, ChessBoard theBoard, int rowDir, int colDir) {
        int i = 1;
        while ((sPosition.getRow()+(i*rowDir) <= 8) && (sPosition.getRow()+(i*rowDir) >= 1) &&
                (sPosition.getColumn()+(i*colDir) <= 8) && (sPosition.getColumn()+(i*colDir) >= 1)) {
            ChessPosition nPosition = new ChessPosition
                    (sPosition.getRow()+(i*rowDir), sPosition.getColumn()+(i*colDir));
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
    }
}
