package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var serverURL = "http://localhost:8080";
        try {
            new ChessClient(serverURL).run();
        } catch (Exception e) {
            System.out.println("Unable to start server.");
        }
    }
}
