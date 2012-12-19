package lyricshow.services;

import lyricshow.data.LibraryConflictException;
import lyricshow.data.SongDAO;
import lyricshow.data.domain.Song;

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

    private SongAdder(){}

    public void updateSong(Song song) throws IOException {
        try {
            dao.updateSong(song);
        } catch (LibraryConflictException e) {
            throw new RuntimeException("LibraryConflictException when updating song: " + song.getTitle(), e);
        }
    }

    public void addSong(Song song) throws IOException, LibraryConflictException {
        dao.addSong(song);
    }

    public void deleteSong(String title) throws IOException {
        try {
            dao.deleteSong(title);
        } catch (LibraryConflictException e) {
            throw new RuntimeException("LibraryConflictException when updating song: " + title, e);
        }
    }
}
