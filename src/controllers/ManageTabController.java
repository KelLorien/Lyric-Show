package controllers;

<<<<<<< HEAD
import data.LibraryConflictException;
import data.domain.Song;
import services.BackUp;
import services.PPTImporter;
import services.SongAdder;
import services.SongList;

=======
>>>>>>> Template
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JOptionPane;

import services.PPTImporter;
import services.SongAdder;
import data.LibraryConflictException;
import data.domain.Song;

/**
 * User: jpipe
 * Date: 12/13/12
 */
public class ManageTabController {

    private static ManageTabController instance = null;

    public static ManageTabController getInstance() {
        if (instance == null)
            instance = new ManageTabController();
        return instance;
    }

    private SongAdder adder = SongAdder.getInstance();
    private PPTImporter importer = PPTImporter.getInstance();
    private SongList songList = SongList.getInstance();

    private ManageTabController() {}

    public void editSong(Song song) {
        saveOrUpdate(song);
    }

    public void saveSong(Song song) {
        saveOrUpdate(song);
    }

    private void saveOrUpdate(Song song) {
        try {
            if (songList.getSongTitles().contains(song.getTitle())) {
                adder.updateSong(song);
            } else {
                adder.addSong(song);
            }
        } catch (IOException e) {
        	e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was a problem updating the file. " + e.getMessage());
        } catch (LibraryConflictException e) {
        	e.printStackTrace();
            JOptionPane.showMessageDialog(null, "A song with this name already exists. " + e.getMessage());
        }
    }

    public void importFromPPT(File targetFile) {
        try {
            for (Song song: importer.importSong(targetFile)) {

                try {
                    adder.addSong(song);
                } catch (LibraryConflictException e) {
                    int selection = JOptionPane.showConfirmDialog(null, "Song entitled: " + song.getTitle() + " already exists.\n" +
                            "Replace with this one? ", "Library Conflict" , JOptionPane.YES_NO_OPTION);
                    if (selection == JOptionPane.OK_OPTION) {
                        adder.updateSong(song);
                    }
                }

            }

        } catch (IOException e) {
        	e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was a problem adding the file. " + e.getMessage());
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "Problem updating a song. " + e.getMessage());
        }
    }

    public void backupLibrary(File target, boolean txt, boolean ppt) {
        try {
            if (txt) {
                BackUp.BackUpTxt(target);
            }
            if (ppt) {
                BackUp.BackUpPPT(target);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not backup. " + e.getMessage());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Could not read library. Some files may be corrupt. " + e.getMessage());
        }
    }

}
