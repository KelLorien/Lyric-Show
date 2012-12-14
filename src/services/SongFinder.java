package services;

/**
 * User: jpipe
 * Date: 11/26/12
 * Time: 5:31 PM
 */
public class SongFinder {

    private static SongFinder instance;

    public static SongFinder getInstance() {
        if (instance == null)
            instance = new SongFinder();
        return instance;
    }

    private SongFinder() {}


}
