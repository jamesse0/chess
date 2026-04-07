package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.DataAccessException;
import model.GameData;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;


public class MySqlGameDAO implements GameDAO{
    Gson serializer = new Gson();
    public MySqlGameDAO(){}
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
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("createGame error");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE game_id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,gameID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int confirmedGameID = resultSet.getInt("game_id");
                String whiteUsername = resultSet.getString("white_username");
                String blackUsername = resultSet.getString("black_username");
                String gameName = resultSet.getString("game_name");
                String gameString = resultSet.getString("game");
                ChessGame game = serializer.fromJson(gameString, ChessGame.class);
                return new GameData(confirmedGameID,whiteUsername,blackUsername,gameName,game);
            }
            else {
                return null;
            }
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("getGame error");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var statement = "SELECT * FROM games";
        ArrayList<GameData> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int gameID = resultSet.getInt("game_id");
                String whiteUsername = resultSet.getString("white_username");
                String blackUsername = resultSet.getString("black_username");
                String gameName = resultSet.getString("game_name");
                String gameString = resultSet.getString("game");
                ChessGame game = serializer.fromJson(gameString, ChessGame.class);
                games.add(new GameData(gameID,whiteUsername,blackUsername,gameName,game));
            }
            return games;
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("listGames error");
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        var statement = "UPDATE games SET white_username = ?, black_username = ?," +
                " game_name = ?, game = ? WHERE game_id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, gameData.whiteUsername());
            preparedStatement.setString(2, gameData.blackUsername());
            preparedStatement.setString(3, gameData.gameName());
            String gameString = serializer.toJson(gameData.game());
            preparedStatement.setString(4, gameString);
            preparedStatement.setInt(5, gameData.gameID());
            int resultSet = preparedStatement.executeUpdate();
        }catch (SQLException | DataAccessException error) {
            throw new DataAccessException("updateGame error");
        }
    }

    @Override
    public void gameOver (Integer gameID) throws DataAccessException {
        var statement = "UPDATE games SET is_game_over = ? WHERE game_id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setBoolean(1,true);
            preparedStatement.setInt(2,gameID);
            preparedStatement.executeUpdate();
        } catch (Exception error) {
            throw new DataAccessException("gameOver error");
        }
    }

    @Override
    public boolean gameStatus(Integer gameID) throws DataAccessException {
        var statement = "SELECT is_game_over FROM games WHERE game_id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1, gameID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                boolean isGameOver = resultSet.getBoolean("is_game_over");
                return isGameOver;
            }
        } catch (Exception error) {
            throw new DataAccessException("gameStatus error");
        }
        return false;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE games";
        try (Connection conn = DatabaseManager.getConnection()) {
            Statement clear = conn.createStatement();
            clear.execute(statement);
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("clear games error");
        }
    }
}
