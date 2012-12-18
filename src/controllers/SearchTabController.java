package controllers;

import data.LibraryConflictException;
import data.domain.Song;
import gui.GUI;
import services.SongAdder;
import services.SongFinder;
import services.SongList;
import services.BackUp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

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

    public Song getSong(String title) {
        try {
            return finder.getSongByTitle(title);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Could not find a song entitled " + title);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Could not read data for song " + title + ". File may be corrupt!" + e.getMessage());
        }
        return new Song();
    }

    public void deleteSong(String songName) {
        try {
            adder.deleteSong(songName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "There was a problem deleting the file. " + e.getMessage());
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "There was a problem deleting the file. " + e.getMessage());
        }
    }

    public List<String> searchByType(String type, String search) {

        try {
            if (type.equals(GUI.AUTHOR)) {     //search by lyricist and author simultaneously
                List<String> titles = new ArrayList<String>();
                titles.addAll(finder.getTitlesByAuthor(search));
                titles.addAll(finder.getTitlesByLyricist(search));
                return titles;
            } else if (type.equals(GUI.KEYWORDS)) {
                return finder.getTitlesByKeyword(search);
            } else if (type.equals(GUI.KEY)) {
                return finder.getTitlesByKey(search);
            } else if (type.equals(GUI.TITLE)) {
                List<String> songList = SongList.getInstance().getSongTitles();
                List<String> results = new ArrayList<String>();
                for (String aSongList : songList) {
                    if (aSongList.toLowerCase().contains(search.toLowerCase())) {
                        results.add(aSongList);
                        JOptionPane.showMessageDialog(null, aSongList);
                    }
                }
                return results;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Could not read from library. " + e.getMessage());
        }

        return Collections.emptyList();
    }
}
