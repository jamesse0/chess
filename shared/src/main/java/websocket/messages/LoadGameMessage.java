package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    ChessGame game;

    public LoadGameMessage (ServerMessage.ServerMessageType serverMessageType) {
        super(serverMessageType);
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

}
