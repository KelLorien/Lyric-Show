package controllers;

/**
 * User: jpipe
 * Date: 12/13/12
 */
public class SlideshowTabController {

    private static SlideshowTabController instance = null;

    public static SlideshowTabController getInstance() {
        if (instance == null)
            instance = new SlideshowTabController();
        return instance;
    }


}
