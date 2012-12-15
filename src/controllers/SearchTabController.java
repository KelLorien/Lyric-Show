package controllers;

import data.domain.Song;
import services.SongAdder;
import services.SongFinder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: jpipe
 * Date: 12/13/12
 */
public class SearchTabController {

    private static SearchTabController instance = null;

    public static SearchTabController getInstance() {
        if (instance == null)
            instance = new SearchTabController();
        return instance;
    }

    private SongFinder finder = SongFinder.getInstance();
    private SongAdder adder = SongAdder.getInstance();

    private SearchTabController() {}

    public void backupLibrary(File target) {
        //TODO: backup library
    }

    public Song getSong(String title) {
        //TODO: get song from songFinder
        return new Song();
    }

    public void deleteSong(String songName) {
        try {
            adder.deleteSong(songName);
        } catch (IOException e) {
            //TODO: error message
        }
    }

    public List<String> searchByType(String type, String search) {
        //TODO: get list of songs from songFinder
        if (type.equals(LyricShowGUI.AUTHOR)) {
            //by author/lyricist
        } else if (type.equals(LyricShowGUI.KEYWORDS)) {
            //by keywords
        } else if (type.equals(LyricShowGUI.KEY)) {
            //by key
        } else if (type.equals(LyricShowGUI.TITLE)) {
            return Arrays.asList(getSong(search).getTitle());
        }

        return Collections.emptyList();
    }
}
