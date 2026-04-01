package websocket.commands;

public class FullUserGameCommand extends UserGameCommand{
    private final PlayerType playerType;
    private final String username;

    public FullUserGameCommand (UserGameCommand.CommandType commandType,
                                String authToken, Integer gameID, String username, PlayerType playerType) {
        super(commandType, authToken, gameID);

        this.playerType = playerType;
        this.username = username;
    }

    public enum PlayerType {
        OBSERVER,
        PLAYER
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public String getUsername() {
        return username;
    }
}
