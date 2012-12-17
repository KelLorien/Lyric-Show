package services;

import data.SongDAO;
import data.domain.Song;
import util.SongStorageParser;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

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

    SongDAO dao = SongDAO.getInstance();

    private SongFinder() {}

    public Song getSongByTitle(String title) throws FileNotFoundException, ParseException {
        return dao.getSong(title);
    }

    public List<String> getTitlesByAuthor(String author) throws ParseException {	
        return dao.getAllSongsWithAttribute(author, SongStorageParser.Tag.AUTHOR);
    }

    public List<String> getTitlesByLyricist(String lyricist) throws ParseException {
        return dao.getAllSongsWithAttribute(lyricist, SongStorageParser.Tag.LYRICIST);
    }

    public List<String> getTitlesByKeyword(String keyword) throws ParseException {
        return dao.getTitlesWithKeyword(keyword);
    }

    public List<String> getTitlesByKey(String key) throws ParseException {
        return dao.getAllSongsWithAttribute(key, SongStorageParser.Tag.MUSICAL_KEY);
    }

}
