package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.content.Context;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.List;

public class SheetsManager {

    private Sheets service;
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private UserManager userManager;
    private GoogleAccountCredential credential;

    public SheetsManager() {
        userManager = objectFactory.getUserManager();
    }

    private List<List<Object>> fetchData(Context context, String sheetID, String range) {

        userManager.getLastAccount(context);
        userManager.initCredential(context);
        credential = userManager.getCredential();
        service = new Sheets(AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential);


        List<List<Object>> values = null;
        ValueRange response = null;

        try {
            response = service.spreadsheets().values().get(sheetID, range).execute();

            values = response.getValues();

            Log.e("FETCH_DATA", "fetchData: " + values);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }

    public List<List<Object>> fetchCategoriesAndGroups (Context context, String sheetID){
        return fetchData(context, sheetID, "Dashboard!H4:O");
    }
}
