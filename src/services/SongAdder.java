package services;

import data.LibraryConflictException;
import data.LibraryWriteException;
import data.SongDAO;
import data.domain.Song;

import java.io.IOException;

/**
 * User: jpipe
 * Date: 11/26/12
 * Time: 5:31 PM
 */
public class SongAdder {

    private static SongAdder instance;

    public static SongAdder getInstance() {
        if (instance == null)
            instance = new SongAdder();
        return instance;
    }

    private SongDAO dao = SongDAO.getInstance();
    private SongList songList = SongList.getInstance();

    private SongAdder(){}

    public void saveOrUpdateSong(Song song) throws IOException, LibraryWriteException, LibraryConflictException {
        if (songList.getSongTitles().contains(song.getTitle())) {
            dao.addSong(song);
        } else {
            dao.updateSong(song);
        }
    }
}
