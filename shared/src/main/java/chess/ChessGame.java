package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard theBoard;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        theBoard = new ChessBoard();
        theBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece thePiece = theBoard.getPiece(startPosition);
        ArrayList<ChessMove> vMoves = (ArrayList<ChessMove>) thePiece.pieceMoves(theBoard, startPosition);
        for (int i = 0; i < vMoves.size(); i++) {
            ChessBoard simBoard = theBoard.clone();
            simulateMove(vMoves.get(i), simBoard);
            if (simulateIsInCheck(thePiece.getTeamColor(), simBoard)) {
                vMoves.remove(i);
                i--;
            }
        }
        return vMoves;
    }



    public ChessPosition myKing(TeamColor myTeam, ChessBoard board) {
        ChessPosition kingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i,j);
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece != null) {
                    if ((currPiece.getPieceType() == ChessPiece.PieceType.KING) && (currPiece.getTeamColor() == myTeam)) {
                        kingPosition = currPosition;
                    }
                }
            }
        }
        return kingPosition;
    }

    public void simulateMove (ChessMove move, ChessBoard board) {
        ChessPiece thePiece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            thePiece = new ChessPiece(thePiece.getTeamColor(), move.getPromotionPiece());
        }
        board.addPiece(move.getEndPosition(), thePiece);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if ((theBoard.getPiece(move.getStartPosition()) == null) ||
                ((theBoard.getPiece(move.getStartPosition()).getTeamColor() != teamTurn))) {
            throw new InvalidMoveException();
        }
        else {
            ArrayList<ChessMove> vMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
            boolean isValid = false;
            for (ChessMove vMove : vMoves) {
                if (vMove.equals(move)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                throw new InvalidMoveException();
            } else {
                ChessPiece thePiece = theBoard.getPiece(move.getStartPosition());
                theBoard.addPiece(move.getStartPosition(), null);
                if (move.getPromotionPiece() != null) {
                    thePiece = new ChessPiece(thePiece.getTeamColor(), move.getPromotionPiece());
                }
                theBoard.addPiece(move.getEndPosition(), thePiece);
                if (teamTurn.equals(TeamColor.WHITE)){
                    teamTurn = TeamColor.BLACK;
                }
                else {
                    teamTurn = TeamColor.WHITE;
                }
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = myKing(teamColor, theBoard);
        ArrayList<ChessPosition> enemyEndPositions = new ArrayList<>();
        if (teamColor == TeamColor.WHITE) {
            enemyEndPositions = listEndPosition(TeamColor.BLACK, theBoard);
        }
        else {
            enemyEndPositions = listEndPosition(TeamColor.WHITE, theBoard);
        }
        for (ChessPosition enemyRange : enemyEndPositions) {
            if (enemyRange.equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    public boolean simulateIsInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = myKing(teamColor, board);
        ArrayList<ChessPosition> enemyEndPositions = new ArrayList<>();
        if (teamColor == TeamColor.WHITE) {
            enemyEndPositions = listEndPosition(TeamColor.BLACK, board);
        }
        else {
            enemyEndPositions = listEndPosition(TeamColor.WHITE, board);
        }
        for (ChessPosition enemyRange: enemyEndPositions) {
            if (enemyRange.equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ChessPosition> listEndPosition(TeamColor team, ChessBoard board) {
        ArrayList<ChessPosition> allEndPositions = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i,j);
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece != null) {
                    if (currPiece.getTeamColor() == team) {
                        ArrayList<ChessMove> currPieceMoves = (ArrayList<ChessMove>) currPiece.pieceMoves(board, currPosition);
                        for (ChessMove currMove: currPieceMoves) {
                            allEndPositions.add(currMove.getEndPosition());
                        }
                    }
                }
            }
        }
        return allEndPositions;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        theBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return theBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(theBoard, chessGame.theBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, theBoard);
    }
}
