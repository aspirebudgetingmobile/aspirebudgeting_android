package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.app.Application;
import android.content.Context;

public class ObjectFactory {

    private static ObjectFactory objectFactory = null;

    private static UserManager userManager;

    private static SheetsManager sheetsManager;

    private SessionConfig sessionConfig;

    private AuthenticateUser authenticateUser;

    private Context context;

    private ObjectFactory() {
    }

    public static synchronized ObjectFactory getInstance() {
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }
        return objectFactory;
    }

    public void initContext(Context context){
        this.context = context.getApplicationContext();
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

    public SessionConfig getSessionConfig() {
        if (sessionConfig == null) {
            sessionConfig = new SessionConfig(context);
        }
        return sessionConfig;
    }

    public AuthenticateUser getAuthenticateUser() {
        if (authenticateUser == null) {
            authenticateUser = new AuthenticateUser(context);
        }
        return authenticateUser;
    }
}
