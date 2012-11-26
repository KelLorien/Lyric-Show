package data;

import data.domain.Song;
import util.Preferences;
import util.SongStorageParser;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static util.SongStorageParser.*;

/**
 * User: jpipe
 * Date: 11/19/12
 */
public class SongDAO {

    private static final String LIBRARY_DIR_NAME = "songs";
    private static final String INDEX_FILE_NAME = "index.lib";

    private static String LIBRARY_URL = Preferences.getLibraryURL();


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

    private File library;

    //private constructor; use the static method above to access this singleton.
    private SongDAO() {
        library = new File(LIBRARY_URL + LIBRARY_DIR_NAME);
        if (!library.mkdir()) {
            System.out.println("INFO: song library already exists");
        }
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
     */
    public ArrayList<String> getAllTitles() {
        ArrayList<String> titles = new ArrayList<String>();

        titles.addAll(Arrays.asList(library.list()));

        return titles;
    }

    /**
     * Gets the song with the given title.
     * @param title the song title of the desired song
     * @return {@link Song} object fully instantiated with all its relevant information.
     */
    public Song getSong(String title) throws FileNotFoundException, LibraryReadException {
        Song song = new Song();
        song.setTitle(title);

        Scanner scanner = getLibraryScanner(title);

        //TODO: instantiate song
        return song;
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
            File newStorageFile = new File(library, song.getTitle());
            System.out.println(newStorageFile.getAbsolutePath());

            if (newStorageFile.createNewFile()) {
                writer = new FileWriter(newStorageFile);
                writer.write(toStorageString(song));
            } else {
                throw new IOException("Could not create storage file: " + newStorageFile.getAbsolutePath());
            }
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

    private Scanner getLibraryScanner(String title) throws FileNotFoundException {
        return new Scanner(new File(library, title));
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
            e.printStackTrace();
        }

        System.out.println(dao.getAllTitles());


    }
}
