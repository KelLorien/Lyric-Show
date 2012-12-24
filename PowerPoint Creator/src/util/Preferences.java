package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * User: jpipe
 * Date: 11/19/12
 */
public class Preferences {

    private static final String PREF_FILE = "prefs.txt";
    private static final String HARD_DEFAULT_LIBRARY = "song_lib/";

    /**
     * Gets the path to the directory where the song library is located. The index file
     * will also be located there.
     * @return String representing the path to the library.
     */
    public static String getLibraryURL() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(PREF_FILE));

            return scanner.nextLine().split(":")[1];
        } catch (FileNotFoundException e) {
            System.err.println("Could not find Preferences File!");
            e.printStackTrace();
            return HARD_DEFAULT_LIBRARY;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }


    }

    public static String getPPTOutURL() {
        return "output/";
    }
}
