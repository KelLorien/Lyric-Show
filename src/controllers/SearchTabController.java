package controllers;

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
}
