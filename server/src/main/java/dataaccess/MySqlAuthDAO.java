package dataaccess;
import java.sql.*;
import model.AuthData;
import model.UserData;

public class MySqlAuthDAO implements AuthDAO{
    public MySqlAuthDAO () throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auths (username,auth_token) VALUES (?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, authData.username());
            preparedStatement.setString(2, authData.authToken());
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            throw new DataAccessException("SQL db error");
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
        } catch (SQLException error) {
            throw new DataAccessException("SQL db error");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE auths";
        try (Connection conn = DatabaseManager.getConnection()) {
            Statement clear = conn.createStatement();
            clear.execute(statement);
        } catch (SQLException error) {
            throw new DataAccessException("SQL db error");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auths (
            id INT NOT NULL AUTO_INCREMENT,
            username VARCHAR(255) NOT NULL,
            auth_token VARCHAR(255) NOT NULL,
            PRIMARY KEY (id),
            INDEX(auth_token),
            INDEX (username)
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
