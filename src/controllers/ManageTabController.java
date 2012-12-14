package controllers;

import data.LibraryConflictException;
import data.domain.Song;
import services.PPTImporter;
import services.SongAdder;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
            //TODO: error message
            //could not save song due to IO exception
        } catch (LibraryConflictException e) {
            //TODO: error message
            //song with this title already exists
            //this should not happen, since SongAdder.saveOrUpdate checks it it exists
        }
    }

    public void importFromPPT(File targetFile) {
        try {
            importer.importSong(targetFile);
        } catch (IOException e) {
            //TODO: error message
            //could not write PPT file
        }
    }

}
