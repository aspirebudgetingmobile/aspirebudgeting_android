package com.aspirebudgetingmobile.aspirebudgeting.utils;

public class ObjectFactory {

    private static ObjectFactory objectFactory = null;

    private static UserManager userManager;

    private static SheetsManager sheetsManager;

    private ObjectFactory() {
    }

    public static synchronized ObjectFactory getInstance() {
        if (objectFactory == null) {
             objectFactory = new ObjectFactory();
        }
        return objectFactory;
    }

    public UserManager getUserManager() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public SheetsManager getSheetsManager() {
        if (sheetsManager == null) {
            sheetsManager = new SheetsManager();
        }
        return sheetsManager;
    }
}
