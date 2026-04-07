package websocket.commands;

import chess.ChessMove;

public class FullUserGameCommand extends UserGameCommand{
    private final PlayerType playerType;
    private final String username;
    private final String color;
    private ChessMove chessMove;
    private boolean gameOver;

    public FullUserGameCommand (UserGameCommand.CommandType commandType,
                                String authToken, Integer gameID, String username, PlayerType playerType, String color){
        super(commandType, authToken, gameID);

        this.playerType = playerType;
        this.username = username;
        this.color = color;
        gameOver = false;
    }

    public enum PlayerType {
        OBSERVER,
        PLAYER
    }

    public String getPlayerTypeString() {
        String response;
        if (playerType == PlayerType.OBSERVER) {
            response = "Observer";
            return response;
        } else {
            response = "Player";
            return response;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getUsername() {
        return username;
    }

    public String getColor() {
        return color;
    }

    public ChessMove getChessMove() {
        return chessMove;
    }

    public void setChessMove(ChessMove chessMove) {
        this.chessMove = chessMove;
    }
}
