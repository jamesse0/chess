package dataaccess;
import java.sql.*;
import model.AuthData;


public class MySqlAuthDAO implements AuthDAO{
    public MySqlAuthDAO () {}

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auths (username,auth_token) VALUES (?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authData.username());
            preparedStatement.setString(2, authData.authToken());
            preparedStatement.executeUpdate();
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("createAuth error");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT * FROM auths WHERE auth_token = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,authToken);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String confirmedAuthToken = resultSet.getString("auth_token");
                String username = resultSet.getString("username");
                return new AuthData(confirmedAuthToken, username);
            }
            else {
                return null;
            }
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("getAuth error");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE auth_token = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,authToken);
            int resultSet = preparedStatement.executeUpdate();
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("deleteAuth error");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE auths";
        try (Connection conn = DatabaseManager.getConnection()) {
            Statement clear = conn.createStatement();
            clear.execute(statement);
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("clear auths error");
        }
    }
}
