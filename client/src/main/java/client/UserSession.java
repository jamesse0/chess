package client;

import chess.ChessGame;

public class UserSession {
    private String authToken;
    private String username;
    private Integer gameID;
    private String teamColor;
    private ChessGame game;

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
}
