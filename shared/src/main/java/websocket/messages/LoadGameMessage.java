package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage {
    ServerMessage.ServerMessageType serverMessageType;

    public LoadGameMessage(ServerMessage.ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessage.ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    ChessGame game;
    boolean gameOver = false;

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
