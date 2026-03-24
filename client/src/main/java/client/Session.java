package client;

import chess.ChessGame;

public class Session {
    private String authToken;
    private String username;
    private Integer gameID;
    private ChessGame.TeamColor teamColor;

    public Session () {
        authToken = null;
        username = null;
        gameID = null;
        teamColor = null;
    }

    public void setAuth (String authToken) {
        this.authToken = authToken;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public void setGameID (int gameID) {
        this.gameID = gameID;
    }

    public void setTeamColor (ChessGame.TeamColor teamColor) {
        this.teamColor = teamColor;
    }

}
