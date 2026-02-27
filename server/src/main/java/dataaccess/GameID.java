package dataaccess;
import java.security.SecureRandom;
public class GameID {
    public static int generateGameID () {
        var sr = new SecureRandom();
        return 1000 + sr.nextInt(8999);
    }
}
