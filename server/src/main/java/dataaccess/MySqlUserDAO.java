package dataaccess;
import java.sql.*;
import model.UserData;


public class MySqlUserDAO implements UserDAO {
    public MySqlUserDAO() {}

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username,password,email) VALUES (?,?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, user.password());
            preparedStatement.setString(3, user.email());
            preparedStatement.executeUpdate();
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("createUser error");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String confirmedUserName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                return new UserData(confirmedUserName, password, email);
            }
            else {
                return null;
            }
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("getUser error");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE users";
        try (Connection conn = DatabaseManager.getConnection()) {
            Statement clear = conn.createStatement();
            clear.execute(statement);
        } catch (SQLException | DataAccessException error) {
            throw new DataAccessException("clear users error");
        }
    }
}
