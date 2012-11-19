package data;

import data.domain.Song;
import util.Preferences;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * User: jpipe
 * WeekDate: 11/19/12
 */
public class SongDAO {

    private static final String LIB_FILE_NAME = "songs.lib";
    private static final String INDEX_FILE_NAME = "index.lib";

    static String LIBRARY_URL = Preferences.getLibraryURI();

    private static SongDAO instance = null;

    private ArrayList<String> titles;

    public static SongDAO getInstance() {
        if (instance == null) {
            instance = new SongDAO();
        }
        return instance;
    }

    public SongDAO() {
        try {
            titles = getAllTitles();
        } catch (LibraryReadException e) {
            //TODO: decide what to do here
        }
    }

    public void writeSong(Song song) throws LibraryWriteException {

        try {
            if (titleExists(song.getTitle())) {
                throw new LibraryWriteException("Song title already exists.");
            }

            writeToLibrary(song);

        } catch (IOException e) {
            e.printStackTrace();
            throw new LibraryWriteException("Could not write song to library", e);
        }
    }

    public boolean titleExists(String title) {
        return titles.contains(title);
    }

    private ArrayList<String> getAllTitles() throws LibraryReadException {
        ArrayList<String> titles = new ArrayList<String>();

        Scanner scanner = null;

        try {
            scanner = getIndexScanner();

            while (scanner.hasNext()) {
                titles.add(extractTagDataFromString(scanner.nextLine(), TITLE));
            }
        } catch (ParseException e) {
            System.err.println(e);
            throw new LibraryReadException("Could not read Index file", e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return titles;
    }

    private Scanner getIndexScanner() {
        try {
            return new Scanner(new File(LIBRARY_URL + INDEX_FILE_NAME));
        } catch (FileNotFoundException e) {
            System.err.println("Index not found!");
            throw new RuntimeException("Index not found", e);
        }
    }

    private FileReader getLibraryScanner() {
        try {
            return new FileReader(LIBRARY_URL + INDEX_FILE_NAME);
        } catch (FileNotFoundException e) {
            System.err.println("Library not found!");
            throw new RuntimeException("Library not found", e);
        }
    }

    private void writeToLibrary(Song song) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(LIBRARY_URL + LIB_FILE_NAME);
            writer.write(toStorageString(song));
        } finally {
            closeQuietly(writer);
        }
    }

    private void closeQuietly(Closeable io) {
        try {
            if (io != null)
                io.close();
        } catch (IOException e) {
            //ignore
        }
    }

    //TODO: methods below here probably belong in a util class.

    private static final String SONG = "[<";
    private static final String SONG_END = ">]";
    private static final String TITLE = "<t>";
    private static final String LYRIC = "<l>";
    private static final String ATT = "<a>";
    private static final String ATT_VAL = "<v>";
    private static final String ATT_LIST = "<ats>";
    private static final String DATE = "<d>";
    private static final String TITLE_END = "<\\t>";
    private static final String LYRIC_END = "<\\l>";
    private static final String ATT_END = "<\\a>";
    private static final String ATT_VAL_END = "<\\v>";
    private static final String ATT_LIST_END = "<\\ats>";
    private static final String DATE_END = "<\\d>";
    private static final String LINE = "\n";

    private String toIndexString(Song song) {
        String ss = SONG + TITLE + song.getTitle() + TITLE_END + LINE;

        ss += DATE + song.getLastUsed().getTime() + DATE_END;

        ss += ATT_LIST;
        Map<String, String> atts = song.getAttributeMap();
        for (String key: atts.keySet()) {
            ss += ATT + key + ATT_END;
            ss += ATT_VAL + atts.get(key) + ATT_VAL_END + LINE;
        }
        ss += ATT_LIST_END;

        return ss += SONG_END;
    }

    private String toStorageString(Song song) {
        return toIndexString(song) + LYRIC + song.getLyrics() + LYRIC_END;
    }


    private String extractTagDataFromString(String line, String tag) throws ParseException {
        String title = null;
        int index = line.indexOf(tag);
        if (index > 0) {
            try {
                title = line.substring(index + tag.length(), line.indexOf(TITLE_END)); //TODO: make ending tag dynamic, change the constants
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Unable to parse title attribute from line:\n" + line +
                        "\nNo closing tag.", line.length()-1);
            }
        }
        return title;
    }

}
