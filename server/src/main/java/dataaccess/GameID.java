package dataaccess;
import java.security.SecureRandom;
public class GameID {
    public static int generateGameID () {
        var sr = new SecureRandom();
        return sr.nextInt(10000);
    }
}
