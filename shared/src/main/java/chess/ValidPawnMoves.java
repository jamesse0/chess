package chess;

import java.util.*;

public class ValidPawnMoves {
    public static List<ChessMove> validMoves(ChessPosition startPosition, ChessPiece thePiece, ChessBoard theBoard) {// returns ChessMove list of valid pawn moves
        List<ChessMove> vMoves = new ArrayList<>();
        if (thePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (startPosition.getRow() == 2) {
                if ((theBoard.getPiece((new ChessPosition(startPosition.getRow()+1, startPosition.getColumn() ))) == null)
                        && (theBoard.getPiece((new ChessPosition(startPosition.getRow()+2, startPosition.getColumn() ))) == null)) {
                    vMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+2, startPosition.getColumn()), null));
                }
            }
            if (startPosition.getRow() == 7) {
                if (theBoard.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn())) == null) {
                    vMoves.addAll(promotionMoves(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn())));
                }
                if (startPosition.getColumn()+1 <= 8) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                        vMoves.addAll(promotionMoves(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1)));
                    }
                }
                if (startPosition.getColumn()-1 >= 1) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                        vMoves.addAll(promotionMoves(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1)));
                    }
                }
            }
            else {
                if (theBoard.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn())) == null) {
                    vMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()), null));
                }
                if (startPosition.getColumn()+1 <= 8) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                        vMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1), null));
                    }
                }
                if (startPosition.getColumn()-1 >= 1) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                        vMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1), null));
                    }
                }
            }
        }
        else {
            if (startPosition.getRow() == 7) {
                if ((theBoard.getPiece((new ChessPosition(startPosition.getRow()-1, startPosition.getColumn() ))) == null)
                        && (theBoard.getPiece((new ChessPosition(startPosition.getRow()-2, startPosition.getColumn() ))) == null)) {
                    vMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-2, startPosition.getColumn()), null));
                }
            }
            if (startPosition.getRow() == 2) {
                if (theBoard.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn())) == null) {
                    vMoves.addAll(promotionMoves(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn())));
                }
                if (startPosition.getColumn()+1 <= 8) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                        vMoves.addAll(promotionMoves(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1)));
                    }
                }
                if (startPosition.getColumn()-1 >= 1) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                        vMoves.addAll(promotionMoves(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1)));
                    }
                }
            }
            else {
                if (theBoard.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn())) == null) {
                    vMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()), null));
                }
                if (startPosition.getColumn()+1 <= 8) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                        vMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1), null));
                    }
                }
                if (startPosition.getColumn()-1 >= 1) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                        vMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1), null));
                    }
                }
            }
        }
        return vMoves;
    }

    public static List<ChessMove> promotionMoves(ChessPosition startPosition, ChessPosition endPosition){ //if it is a promotion move this returns a ChessMove list of all promotion moves
        List<ChessMove> pMoves = new ArrayList<>();
        pMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        pMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        pMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        pMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        return pMoves;
    }
}
