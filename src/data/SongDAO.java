package data;

import data.domain.Song;
import util.Preferences;
import util.SongStorageParser;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static util.SongStorageParser.*;

/**
 * User: jpipe
 * Date: 11/19/12
 *
 * Handles all reading and writing to the library.
 * "DAO" stands for Data Access Object.
 */
public class SongDAO {

    private static final String LIBRARY_DIR_NAME = "songs";
    private static final String INDEX_FILE_NAME = "index.lib";

    private static String STORAGE_URL = Preferences.getLibraryURL();


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
        library = new File(STORAGE_URL + LIBRARY_DIR_NAME);
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
    public List<String> getAllTitles() {
        ArrayList<String> titles = new ArrayList<String>();

        titles.addAll(Arrays.asList(library.list()));

        return titles;
    }

    /**
     * Gets the song with the given title.
     * @param title the song title of the desired song
     * @return {@link Song} object fully instantiated with all its relevant information.
     * @throws FileNotFoundException if the file for the associate song is not found.
     * @throws LibraryReadException if the file could not be read or parsed. Check the for the cause
     * ({@link Throwable#cause}) when thrown if the reason is relevant.
     */
    public Song getSong(String title) throws FileNotFoundException, LibraryReadException {
        Song song = new Song();
        song.setTitle(title);

        Scanner scanner = getLibraryScanner(title);

        String metaLine = scanner.nextLine();

        try {
            song.addAllKeywords(SongStorageParser.extractAllKeywords(metaLine));
            song.setLastUsed(Long.parseLong(SongStorageParser.extractTagDataFromString(metaLine, Tag.DATE)));

            String lyrics = "";
            while(scanner.hasNext()) {
                lyrics += scanner.nextLine() + "\n";
            }

            song.setLyrics(lyrics);

        } catch (ParseException e) {
            throw new LibraryReadException("Could not read from library.", e);
        } finally {
            scanner.close();
        }

        return song;
    }

    public List<String> getTitlesWithKeyword(String key) throws LibraryReadException {
        ArrayList<String> titles =  new ArrayList<String>();

        Scanner indexScanner = getIndexScanner();

        try {
            while (indexScanner.hasNext()){
                String currentTitle = indexScanner.nextLine();
                if (SongStorageParser.extractAllKeywords(indexScanner.nextLine()).contains(key)) {
                    titles.add(currentTitle);
                }
            }
        } catch (ParseException e) {
            throw new LibraryReadException("Could not read from library.", e);
        } finally {
            indexScanner.close();
        }

        return titles;
    }

    private void writeToIndex(Song song) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(STORAGE_URL + INDEX_FILE_NAME, true);
            writer.append(toIndexString(song));
        } finally {
            closeQuietly(writer);
        }
    }

    private void writeToLibrary(Song song) throws IOException {
        FileWriter writer = null;
        try {
            File newStorageFile = new File(library, song.getTitle());

            if (newStorageFile.createNewFile()) {
                writer = new FileWriter(newStorageFile);
                writer.write(toFullStorageString(song));
            } else {
                throw new IOException("Could not create storage file: " + newStorageFile.getAbsolutePath());
            }
        } finally {
            closeQuietly(writer);
        }
    }

    private Scanner getIndexScanner() {
        try {
            return new Scanner(new File(STORAGE_URL + INDEX_FILE_NAME));
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
            System.err.println("Error closing writer");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println();
        SongDAO dao = SongDAO.getInstance();
        Song song = new Song();
        song.setTitle("testing");
        song.setLastDateToNow();
        song.addKeyword("keywuuuuuuuuuuuurd", "another won");
        song.setLyrics("A thousand times I've failed\n" +
                "Still your mercy remains\n" +
                "And should I stumble again\n" +
                "Still I'm caught in your grace\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "\n" +
                "My heart and my soul, I give You control\n" +
                "Consume me from the inside out Lord\n" +
                "Let justice and praise, become my embrace\n" +
                "To love You from the inside out\n" +
                "\n" +
                "Your will above all else, my purpose remains\n" +
                "The art of losing myself in bringing you praise\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "\n" +
                "My heart, my soul, Lord I give you control\n" +
                "Consume me from the inside out Lord\n" +
                "Let justice and praise become my embrace\n" +
                "To love You from the inside out\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "And the cry of my heart is to bring You praise\n" +
                "From the inside out, O my soul cries out\n" +
                "\n" +
                "My Soul cries out to You\n" +
                "My Soul cries out to You\n" +
                "to You, to You\n" +
                "\n" +
                "My heart, my soul, Lord I give you control\n" +
                "Consume me from the inside out Lord\n" +
                "Let justice and praise become my embrace\n" +
                "To love You from the inside out\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "And the cry of my heart is to bring You praise\n" +
                "From the inside out, O my soul cries out\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "And the cry of my heart is to bring You praise\n" +
                "From the inside out, O my soul cries out\n" +
                "From the inside out, O my soul cries out\n" +
                "From the inside out, O my soul cries out.");

        try {
            dao.addSong(song);
            song.setTitle("next");
            dao.addSong(song);
        } catch (LibraryWriteException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(dao.getTitlesWithKeyword("another won"));
        } catch (LibraryReadException e) {
            e.printStackTrace();
        }

        try {
            Song gotten = dao.getSong(song.getTitle());
            System.out.println("Title: " + gotten.getTitle());
            System.out.println("Words: " + gotten.getKeywords());
            System.out.println("Last Played: " + gotten.getLastUsed().toString());
            System.out.println("Lyrics: " + gotten.getLyrics());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (LibraryReadException e) {
            e.printStackTrace();
        }

        File lib = new File(STORAGE_URL + LIBRARY_DIR_NAME);

        for (String title: dao.getAllTitles()) {
            if(!new File(lib, title).delete())
                System.err.println("\""+title + "\" storage not deleted!");
        }

        if(!lib.delete())
            System.err.println("lib not deleted!");

        if(!new File(STORAGE_URL + INDEX_FILE_NAME).delete())
            System.err.println("index not deleted!");
    }
}
