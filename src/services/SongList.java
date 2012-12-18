package services;

import data.SongDAO;

import java.util.Collections;
import java.util.List;

/**
 * User: jpipe
 * Date: 12/10/12
 */
public class SongList {

    private static SongList instance;

    public static SongList getInstance() {
        if(instance == null)
            instance = new SongList();
        return instance;
    }

    private SongDAO dao = SongDAO.getInstance();

    //private constructor. Use the static method above (getInstance) to get a reference to this object.
    private SongList() {}

    public List<String> getSongTitles(){
        List<String> titles = dao.getAllTitles();
        Collections.sort(titles);
        return titles;
    }
}
