package chess;

import java.util.*;

public class ValidPawnMoves {
    public static List<ChessMove> validMoves(ChessPosition sPosition, ChessPiece thePiece, ChessBoard theBoard) {// returns ChessMove list of valid pawn moves
        List<ChessMove> listValidMoves = new ArrayList<>();
        if (thePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (sPosition.getRow() == 2) {
                if ((theBoard.getPiece((new ChessPosition(sPosition.getRow()+1, sPosition.getColumn() ))) == null)
                        && (theBoard.getPiece((new ChessPosition(sPosition.getRow()+2, sPosition.getColumn() ))) == null)) {
                    listValidMoves.add(new ChessMove(sPosition, new ChessPosition(sPosition.getRow()+2, sPosition.getColumn()), null));
                }
            }
            if (sPosition.getRow() == 7) {
                if (theBoard.getPiece(new ChessPosition(sPosition.getRow()+1, sPosition.getColumn())) == null) {
                    listValidMoves.addAll(proMoves(sPosition, new ChessPosition(sPosition.getRow()+1, sPosition.getColumn())));
                }
                if (sPosition.getColumn()+1 <= 8) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(sPosition.getRow()+1, sPosition.getColumn()+1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                        listValidMoves.addAll(proMoves(sPosition, new ChessPosition(sPosition.getRow()+1, sPosition.getColumn()+1)));
                    }
                }
                if (sPosition.getColumn()-1 >= 1) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(sPosition.getRow()+1, sPosition.getColumn()-1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                        listValidMoves.addAll(proMoves(sPosition, new ChessPosition(sPosition.getRow()+1, sPosition.getColumn()-1)));
                    }
                }
            }
            else {
                if (theBoard.getPiece(new ChessPosition(sPosition.getRow()+1, sPosition.getColumn())) == null) {
                    listValidMoves.add(new ChessMove(sPosition, new ChessPosition(sPosition.getRow()+1, sPosition.getColumn()), null));
                }
                if (sPosition.getColumn()+1 <= 8) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(sPosition.getRow()+1, sPosition.getColumn()+1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                        listValidMoves.add(new ChessMove(sPosition, new ChessPosition(sPosition.getRow()+1, sPosition.getColumn()+1), null));
                    }
                }
                if (sPosition.getColumn()-1 >= 1) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(sPosition.getRow()+1, sPosition.getColumn()-1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK)) {
                        listValidMoves.add(new ChessMove(sPosition, new ChessPosition(sPosition.getRow()+1, sPosition.getColumn()-1), null));
                    }
                }
            }
        }
        else {
            if (sPosition.getRow() == 7) {
                if ((theBoard.getPiece((new ChessPosition(sPosition.getRow()-1, sPosition.getColumn() ))) == null)
                        && (theBoard.getPiece((new ChessPosition(sPosition.getRow()-2, sPosition.getColumn() ))) == null)) {
                    listValidMoves.add(new ChessMove(sPosition, new ChessPosition(sPosition.getRow()-2, sPosition.getColumn()), null));
                }
            }
            if (sPosition.getRow() == 2) {
                if (theBoard.getPiece(new ChessPosition(sPosition.getRow()-1, sPosition.getColumn())) == null) {
                    listValidMoves.addAll(proMoves(sPosition, new ChessPosition(sPosition.getRow()-1, sPosition.getColumn())));
                }
                if (sPosition.getColumn()+1 <= 8) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(sPosition.getRow()-1, sPosition.getColumn()+1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                        listValidMoves.addAll(proMoves(sPosition, new ChessPosition(sPosition.getRow()-1, sPosition.getColumn()+1)));
                    }
                }
                if (sPosition.getColumn()-1 >= 1) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(sPosition.getRow()-1, sPosition.getColumn()-1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                        listValidMoves.addAll(proMoves(sPosition, new ChessPosition(sPosition.getRow()-1, sPosition.getColumn()-1)));
                    }
                }
            }
            else {
                if (theBoard.getPiece(new ChessPosition(sPosition.getRow()-1, sPosition.getColumn())) == null) {
                    listValidMoves.add(new ChessMove(sPosition, new ChessPosition(sPosition.getRow()-1, sPosition.getColumn()), null));
                }
                if (sPosition.getColumn()+1 <= 8) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(sPosition.getRow()-1, sPosition.getColumn()+1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                        listValidMoves.add(new ChessMove(sPosition, new ChessPosition(sPosition.getRow()-1, sPosition.getColumn()+1), null));
                    }
                }
                if (sPosition.getColumn()-1 >= 1) {
                    ChessPiece otherPiece = theBoard.getPiece(new ChessPosition(sPosition.getRow()-1, sPosition.getColumn()-1));
                    if ((otherPiece != null) && (otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
                        listValidMoves.add(new ChessMove(sPosition, new ChessPosition(sPosition.getRow()-1, sPosition.getColumn()-1), null));
                    }
                }
            }
        }
        return listValidMoves;
    }

    public static List<ChessMove> proMoves(ChessPosition sPosition, ChessPosition endPosition){ //if it is a promotion move this returns a ChessMove list of all promotion moves
        List<ChessMove> promotionMoves = new ArrayList<>();
        promotionMoves.add(new ChessMove(sPosition, endPosition, ChessPiece.PieceType.QUEEN));
        promotionMoves.add(new ChessMove(sPosition, endPosition, ChessPiece.PieceType.BISHOP));
        promotionMoves.add(new ChessMove(sPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        promotionMoves.add(new ChessMove(sPosition, endPosition, ChessPiece.PieceType.ROOK));
        return promotionMoves;
    }
}
