package data;

import data.domain.Song;
import util.Preferences;
import util.SongStorageParser;

import java.io.*;
import java.text.ParseException;
import java.util.*;

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
    private ArrayList<String> allTitles = new ArrayList<String>();

    //private constructor; use the getInstance() to access this singleton.
    private SongDAO() {
        library = new File(STORAGE_URL + LIBRARY_DIR_NAME);
        library.mkdir();
        if (!library.canRead() && !library.canWrite()) {
            throw new RuntimeException("Library is not readable!");
        }

        allTitles.addAll(Arrays.asList(library.list()));
    }

    /**
     * Adds a song to the library. DOES NOT check that the song title is unique; do not call
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

        allTitles.add(song.getTitle());
    }

    /**
     * Gets all titles of songs currently in the library. Sorts the running list of stored songs before
     * returning (this sorting has no effect on the physical library).
     * @return ArrayList of Strings, representing each song title currently stored in the library in
     * alphabetical order.
     */
    public List<String> getAllTitles() {
        Collections.sort(allTitles);
        return new ArrayList<String>(allTitles);
    }

    /**
     * Gets the song with the given title.
     * @param title the song title of the desired song
     * @return {@link Song} object fully instantiated with all its relevant information.
     * @throws FileNotFoundException if the file for the associate song is not found.
     * @throws LibraryReadException if the file could not be read or parsed. Check the for the cause
     * ({@link Throwable#cause}) when thrown if the reason is relevant.
     */
    public Song getSong(String title) throws FileNotFoundException, ParseException {
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

        } finally {
            scanner.close();
        }

        return song;
    }

    /**
     * Determines which songs have the given keyword, and returns a list of all those song's titles.
     * @param key keyword by which to filter song titles.
     * @return {@link List} of each song title corresponding to each song which has the given keyword.
     * @throws LibraryReadException if there was an error in reading or parsing the
     */
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

    /**
     * Gets a {@link Map} of all song's {@link Song#lastUsed} to the respective song's title.
     *
     * @return {@link Map} of {@link Date} objects to Strings.
     */
    public Map<Date, String> getAllTitlesToLastUsedDatesMap() throws LibraryReadException {
        Map<Date, String> titleToDate = new HashMap<Date, String>();

        Scanner indexScanner = getIndexScanner();

        try {
            while (indexScanner.hasNext()) {
                titleToDate.put(new Date(
                        Long.parseLong(SongStorageParser.extractTagDataFromString(indexScanner.nextLine(), Tag.DATE))
                ), indexScanner.nextLine());
            }
        } catch (ParseException e) {
            throw new LibraryReadException("Could not read index file. ", e);
        } finally {
            indexScanner.close();
        }

        return titleToDate;
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

    private void closeQuietly(Closeable io) {
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
        song.setLastUsed(1L);
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
            song.setLastUsed((long) (Long.MAX_VALUE * Math.random()));
            dao.addSong(song);
        } catch (LibraryWriteException e) {
            e.printStackTrace();
        }

        System.out.println(dao.getAllTitles());

        try {
            System.out.println(dao.getAllTitlesToLastUsedDatesMap());
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
        } catch (ParseException e) {
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
