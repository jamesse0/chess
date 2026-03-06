package dataaccess;
import java.sql.*;
import model.UserData;

public class MySqlUserDAO implements UserDAO {
    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
            id INT NOT NULL AUTO_INCREMENT,
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (id),
            INDEX(username)
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
