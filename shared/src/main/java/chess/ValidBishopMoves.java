package chess;
import java.util.*;
public class ValidBishopMoves {

    public static List<ChessMove> validMoves(ChessPosition sPos, ChessPiece thePiece, ChessBoard theBoard) {
        List<ChessMove> listVMoves = new ArrayList<>();
        ValidRookMoves.getDirection(listVMoves,sPos,thePiece,theBoard,1,1);
        ValidRookMoves.getDirection(listVMoves,sPos,thePiece,theBoard,1,-1);
        ValidRookMoves.getDirection(listVMoves,sPos,thePiece,theBoard,-1,1);
        ValidRookMoves.getDirection(listVMoves,sPos,thePiece,theBoard,-1,-1);
        return listVMoves;
    }
}
