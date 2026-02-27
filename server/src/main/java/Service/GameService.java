package Service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.GameID;
import model.GameData;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService (GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGamesService (LogoutRequest listReq) throws DataAccessException {
        ListGamesResult games;
        if ((listReq.authToken() == null) ||
                (authDAO.getAuth(listReq.authToken()) == null)) {
            throw new DataAccessException("unauthorized");
        }
        else {
            games = new ListGamesResult(gameDAO.listGames());
        }
        return games;
    }

    public CreateGameResult createGameService (CreateGameRequest gameReq, String authToken) throws DataAccessException {
        CreateGameResult game;
        if ((authToken == null) ||
                (authDAO.getAuth(authToken) == null)) {
            throw new DataAccessException("unauthorized");
        }
        if (gameReq.gameName() == null) {
            throw new DataAccessException("bad request");
        }
        else {
            int gameID = GameID.generateGameID();
            GameData newGame = new GameData(gameID,null,null, gameReq.gameName(), new ChessGame());
            gameDAO.createGame(newGame);
            game = new CreateGameResult(newGame.gameID());
        }
        return game;
    }

    public ClearResult joinGameService (JoinGameRequest joinReq, String authToken) throws DataAccessException {
        ClearResult result = new ClearResult();
        if ((authToken == null) ||
                (authDAO.getAuth(authToken) == null)) {
            throw new DataAccessException("unauthorized");
        }
        if ((joinReq.gameID() == 0) || (joinReq.playerColor() == null) || (gameDAO.getGame(joinReq.gameID())==null)
                || ((!joinReq.playerColor().equals("WHITE")) && (!joinReq.playerColor().equals("BLACK")))) {
            throw new DataAccessException("bad request");
        }

        GameData gameData = gameDAO.getGame(joinReq.gameID());

        if (joinReq.playerColor().equals("WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new DataAccessException("already taken");
            }
            else {
                GameData update = new GameData(gameData.gameID(), authDAO.getAuth(authToken).username(),
                        gameData.blackUsername(), gameData.gameName(), gameData.game());
                gameDAO.updateGame(update);
            }
        }
        else {
            if (gameData.blackUsername() != null) {
                throw new DataAccessException("already taken");
            }
            else {
                GameData update = new GameData(gameData.gameID(), gameData.whiteUsername(),
                        authDAO.getAuth(authToken).username(), gameData.gameName(), gameData.game());
                gameDAO.updateGame(update);
            }
        }
        return result;
    }
}
