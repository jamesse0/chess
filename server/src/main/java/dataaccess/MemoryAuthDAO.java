package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> authorizations = new HashMap<>();
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authorizations.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authorizations.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authorizations.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        authorizations.clear();
    }
}
