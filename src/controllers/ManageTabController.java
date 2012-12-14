package controllers;

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
}
