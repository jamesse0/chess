package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import java.sql.*;
import java.util.Collection;
import java.util.List;

public class MySqlGameDAO implements GameDAO{
    Gson serializer = new Gson();
    public MySqlGameDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO games (game_id,white_username,black_username,game_name, game) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, gameData.gameID());
            preparedStatement.setString(2, gameData.whiteUsername());
            preparedStatement.setString(3, gameData.blackUsername());
            preparedStatement.setString(4, gameData.gameName());
            String game = serializer.toJson(gameData.game());
            preparedStatement.setString(5, game);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("SQL db error");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
            game_id INT NOT NULL,
            white_username VARCHAR(255),
            black_username VARCHAR(255),
            game_name VARCHAR(255) NOT NULL,
            game TEXT,
            PRIMARY KEY (game_id)
            )
            
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("SQL db error");
        }
    }
}
