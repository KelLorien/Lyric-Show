package data;

import data.domain.Song;
import util.Preferences;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import static util.SongStorageParser.*;

/**
 * User: jpipe
 * Date: 11/19/12
 */
public class SongDAO {

    private static final String LIBRARY_FILE_NAME = "songs.lib";
    private static final String INDEX_FILE_NAME = "index.lib";

    private static String LIBRARY_URL = Preferences.getLibraryURI();

    private static SongDAO instance = null;

    /**
     * Gets the singleton instance of this class.
     */
    public static SongDAO getInstance() {
        if (instance == null) {
            instance = new SongDAO();
        }
        return instance;
    }

    //private constructor; use the static method above to access this singleton.
    private SongDAO() {
    }

    /**
     * Adds a song to the database. DOES NOT check that the song title is unique; do not call
     * without first confirming that title is unique.
     * @param song new {@link Song} to be written to the library
     * @throws LibraryWriteException if there was an error writing to the library (usually would be caused
     * by a {@link IOException}).
     */
    public void addSong(Song song) throws LibraryWriteException {
        try {
            writeToIndex(song);
        } catch (IOException e) {
            System.err.println("Could not write to index!");
            throw new LibraryWriteException("Could not write to index", e);
        }
        try {
            writeToLibrary(song);
        } catch (IOException e) {
            System.err.println("Could not write to library!");
            throw new LibraryWriteException("Could not write to library", e);
        }
    }

    /**
     * Gets all titles of songs currently in the database.
     * @return ArrayList of Strings, representing each song title currently stored in the library.
     * @throws LibraryReadException if there was an error reading the index.
     */
    public ArrayList<String> getAllTitles() throws LibraryReadException {
        ArrayList<String> titles = new ArrayList<String>();

        Scanner scanner = null;

        try {
            scanner = getIndexScanner();

            Tag tag = Tag.TITLE;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.contains(tag.start())) {
                    titles.add(extractTagDataFromString(line, tag));
                }
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

    private void writeToIndex(Song song) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(LIBRARY_URL + INDEX_FILE_NAME, true);
            writer.append(toIndexString(song));
        } finally {
            closeQuietly(writer);
        }
    }

    private void writeToLibrary(Song song) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(LIBRARY_URL + LIBRARY_FILE_NAME, true);
            writer.append(toStorageString(song));
        } finally {
            closeQuietly(writer);
        }
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

    private void closeQuietly(Writer io) {
        try {
            if (io != null)
                io.close();
        } catch (IOException e) {
            //ignore
        }
    }

    public static void main(String[] args) {

        SongDAO dao = SongDAO.getInstance();
        Song song = new Song();
        song.setTitle("testing");
        song.setLastDateToNow();
        song.addKeyword("keywuuuuuuuuuuuurd");
        song.setLyrics("lkjalksjdlfj\nalsallyourbasekjsdf\nalsdivoia\nalsikivhqwhevhh\nalisdf\n");
        try {
            dao.addSong(song);
            song.setTitle("new title");
            dao.addSong(song);
        } catch (LibraryWriteException e) {
            System.err.println("FAILED WRITE\n"+e);
        }

        try {
            System.out.println(dao.getAllTitles());
        } catch (LibraryReadException e) {
            System.err.println("FAILED READ\n"+e);
        }

        File ind = new File(LIBRARY_URL + INDEX_FILE_NAME);
        File lib = new File(LIBRARY_URL + LIBRARY_FILE_NAME);

        if (ind.exists() && ind.delete()) {
            System.out.println("ind deleted");
        } else {
            System.out.println("ind not deleted");
        }

        if (lib.exists() && lib.exists())
            System.out.println("lib deleted");
        else  {
            System.out.println("lib not deleted");
        }
    }
}
