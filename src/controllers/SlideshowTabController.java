package controllers;

import services.PPTBuilder;

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

    public void createSlideShow(List<String> titles, File target) {
        try {
            pptBuilder.buildPPT(titles, target);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
