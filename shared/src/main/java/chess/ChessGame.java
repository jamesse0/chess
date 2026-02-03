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
        chess.ChessPiece thePiece = theBoard.getPiece(startPosition);
        ArrayList<ChessMove> vMoves = (ArrayList<ChessMove>) thePiece.pieceMoves(theBoard, startPosition);
        for (int i = vMoves.size() - 1; i >= 0; i--) {

        }

        return List.of();
    }

    public ChessPosition myKing(TeamColor myTeam) {
        ChessPosition kingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; i <= 8; i++) {
                ChessPosition currPosition = new ChessPosition(i,j);
                ChessPiece currPiece = theBoard.getPiece(currPosition);
                if (currPiece != null) {
                    if ((currPiece.getPieceType() == ChessPiece.PieceType.KING) && (currPiece.getTeamColor() == myTeam)) {
                        kingPosition = currPosition;
                    }
                }
            }
        }
        return kingPosition;
    }

    public ChessBoard simulateMove (ChessMove move, ChessBoard givenBoard) {
        ChessBoard board = givenBoard;
        ChessPiece thePiece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            thePiece = new ChessPiece(thePiece.getTeamColor(), move.getPromotionPiece());
        }
        board.addPiece(move.getEndPosition(), thePiece);
        return board;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece thePiece = theBoard.getPiece(move.getStartPosition());
        theBoard.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            thePiece = new ChessPiece(thePiece.getTeamColor(), move.getPromotionPiece());
        }
        theBoard.addPiece(move.getEndPosition(), thePiece);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = myKing(teamColor);
        ArrayList<ChessPosition> enemyEndPositions = new ArrayList<>();
        if (teamColor == TeamColor.WHITE) {
            enemyEndPositions = listEndPosition(TeamColor.BLACK);
        }
        else {
            enemyEndPositions = listEndPosition(TeamColor.WHITE);
        }
        for (ChessPosition enemyRange: enemyEndPositions) {
            if (enemyRange == kingPosition) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ChessPosition> listEndPosition(TeamColor team) {
        ArrayList<ChessPosition> allEndPositions = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i,j);
                ChessPiece currPiece = theBoard.getPiece(currPosition);
                if (currPiece != null) {
                    if (currPiece.getTeamColor() != team) {
                        ArrayList<ChessMove> currPieceMoves = (ArrayList<ChessMove>) currPiece.pieceMoves(theBoard, currPosition);
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
