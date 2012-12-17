package controllers;

import data.LibraryConflictException;
import data.domain.Song;
import services.PPTImporter;
import services.SongAdder;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

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

    private ManageTabController() {}

    public void editSong(Song song) {
        saveOrUpdate(song);
    }

    public void saveSong(Song song) {
        saveOrUpdate(song);
    }

    private void saveOrUpdate(Song song) {
        try {
            adder.saveOrUpdateSong(song);
        } catch (IOException e) {
        	JOptionPane.showMessageDialog(null, "There was a problem updating the file");
        } catch (LibraryConflictException e) {
        	JOptionPane.showMessageDialog(null, "A song with this name already exists");
        }
    }

    public void importFromPPT(File targetFile) {
        try {
            for (Song song: importer.importSong(targetFile)) {
                saveOrUpdate(song);
            }

        } catch (IOException e) {
        	JOptionPane.showMessageDialog(null, "There was a problem adding the file");
        }
    }

}
