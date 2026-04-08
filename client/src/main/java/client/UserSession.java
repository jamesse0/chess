package client;

import chess.ChessGame;
import websocket.commands.FullUserGameCommand;

public class UserSession {
    private String authToken;
    private String username;
    private Integer gameID;
    private String teamColor;
    private ChessGame game;
    private FullUserGameCommand.PlayerType playerType;

    public UserSession() {
        authToken = null;
        username = null;
        gameID = null;
        teamColor = null;
        game = null;
    }

    public void setAuth (String authToken) {
        this.authToken = authToken;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public void setGameID (Integer gameID) {
        this.gameID = gameID;
    }

    public void setTeamColor (String teamColor) {
        this.teamColor = teamColor;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGame (ChessGame theGame) {
        game = theGame;
    }

    public ChessGame getGame() {
        return game;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public FullUserGameCommand.PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(FullUserGameCommand.PlayerType playerType) {
        this.playerType = playerType;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
