package websocket.commands;

public class FullUserGameCommand extends UserGameCommand{
    private final PlayerType playerType;
    private final String username;
    private final String color;

    public FullUserGameCommand (UserGameCommand.CommandType commandType,
                                String authToken, Integer gameID, String username, PlayerType playerType, String color) {
        super(commandType, authToken, gameID);

        this.playerType = playerType;
        this.username = username;
        this.color = color;
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
        }
        else {
            response = "Player";
            return response;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getColor() {
        return color;
    }
}
