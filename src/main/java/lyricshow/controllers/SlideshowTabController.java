package lyricshow.controllers;

import lyricshow.services.History;
import lyricshow.services.PPTBuilder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: jpipe
 * Date: 12/13/12
 */
public class SlideshowTabController {

    public static final String BLANK_SLIDE_TITLE = "[BLANK SLIDE]";

    private static SlideshowTabController instance = null;

    public static SlideshowTabController getInstance() {
        if (instance == null)
            instance = new SlideshowTabController();
        return instance;
    }

    private PPTBuilder pptBuilder = PPTBuilder.getInstance();

    private SlideshowTabController() {}

    public void createSlideShow(List<String> titles, File target, String fileName) {
        try {
            History history = History.getInstance();
            pptBuilder.buildPPT(titles, target, fileName);
            history.updateHistory(titles);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not create slideshow. " + e.getMessage());
        }
    }
}
