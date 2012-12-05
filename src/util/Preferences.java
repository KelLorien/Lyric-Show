package util;

/**
 * User: jpipe
 * Date: 11/19/12
 */
public class Preferences {

    //TODO: get from a preferences file
    /**
     * Gets the path to the directory where the song library is located. The index file
     * will also be located there.
     * @return String representing the path to the library.
     */
    public static String getLibraryURL() {
        return "song_lib/";
    }

    public static String getPPTOutURL() {
        return "test_output";
    }
}
