package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pCol;
    private final PieceType pType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        pCol = pieceColor;
        pType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pCol == that.pCol && pType == that.pType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pCol, pType);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pCol;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece((myPosition));
        if (piece.getPieceType() == PieceType.BISHOP) {
            return ValidBishopMoves.validMoves(myPosition,piece,board); //implement here
        }
        else if (piece.getPieceType() == PieceType.QUEEN) {
           List<ChessMove> diagMoves = ValidBishopMoves.validMoves(myPosition,piece,board);
           List<ChessMove> strMoves = ValidRookMoves.validMoves(myPosition,piece,board);
           List<ChessMove> allMoves = new ArrayList<>();
           allMoves.addAll(diagMoves);
           allMoves.addAll(strMoves);
           return allMoves;
        }
        else if (piece.getPieceType() == PieceType.KNIGHT) {
            return ValidKnightMoves.validMoves(myPosition,piece,board);
        }
        else if (piece.getPieceType() == PieceType.KING) {
            return ValidKingMoves.validMoves(myPosition,piece,board);
        }
        else if (piece.getPieceType() == PieceType.ROOK){
            return ValidRookMoves.validMoves(myPosition,piece,board);
        }
        else {
            return ValidPawnMoves.validMoves(myPosition, piece, board);
        }
    }
}
