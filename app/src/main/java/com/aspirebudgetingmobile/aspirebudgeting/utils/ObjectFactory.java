package com.aspirebudgetingmobile.aspirebudgeting.utils;

public class ObjectFactory {

    private static ObjectFactory objectFactory = null;

    private static UserManager userManager;

    private ObjectFactory() {
    }

    public static synchronized ObjectFactory getInstance() {
        if (objectFactory == null){
            return objectFactory = new ObjectFactory();
        } else {
            return objectFactory;
        }
    }

    public UserManager getUserManager(){
        if (userManager == null){
            userManager = new UserManager();
            return userManager;
        } else {
            return userManager;
        }
    }
}
