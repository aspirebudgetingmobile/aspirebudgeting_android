package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.aspirebudgetingmobile.aspirebudgeting.models.DashboardCardsModel;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SheetsManager {

    private Sheets service;
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private UserManager userManager;
    private GoogleAccountCredential credential;

    public SheetsManager() {
        userManager = objectFactory.getUserManager();
    }

    private List<List<Object>> fetchData(Context context, String sheetID, String range) {

        List<Object> fakeBuffer = new ArrayList<>();
        fakeBuffer.add("FakeBuffer");

        userManager.getLastAccount(context);
        userManager.initCredential(context);
        credential = userManager.getCredential();
        service = new Sheets(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential);

        List<List<Object>> values = null;
        ValueRange response = null;

        try {
            response = service.spreadsheets().values().get(sheetID, range).execute();

            values = response.getValues();

            Log.e("FETCH_DATA", "fetchData: " + values);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(values).add(fakeBuffer);
        return values;
    }

    public List<DashboardCardsModel> fetchCategoriesAndGroups (Context context, String sheetID){
        return parseFetchedData(fetchData(context, sheetID, "Dashboard!H4:O"));
    }

    private List<DashboardCardsModel> parseFetchedData(List<List<Object>> fetchedData){

        List<DashboardCardsModel> list = new ArrayList<>();
        JSONArray parsedData = new JSONArray();


        try {
            JSONObject innerObject = null;
            List<String> categoryName = null;
            List<String> budgeted = null;
            List<String> spent = null;
            List<String> available = null;
            String name = "";

            for (int i = 0; i < fetchedData.size(); i++) {

                List<Object> test = fetchedData.get(i);

                if (test.size() == 1) {

                    if (innerObject != null) {
                        innerObject.put("categoryName", categoryName);
                        innerObject.put("budgeted", budgeted);
                        innerObject.put("spent", spent);
                        innerObject.put("available", available);

                        DashboardCardsModel model = new DashboardCardsModel(name,
                                categoryName,
                                budgeted,
                                spent,
                                available);

                        list.add(model);
                    }

                    if (innerObject != null)
                        parsedData.put(innerObject);

                    innerObject = new JSONObject();
                    categoryName = new ArrayList<>();
                    budgeted = new ArrayList<>();
                    spent = new ArrayList<>();
                    available = new ArrayList<>();

                    name =  test.toString().replace("[", "").replace("]", "");
                    innerObject.put("name",name);

                } else {
                    Objects.requireNonNull(categoryName).add(String.valueOf(test.get(0)));
                    budgeted.add(String.valueOf(test.get(7)));
                    spent.add(String.valueOf(test.get(4)));
                    available.add(String.valueOf(test.get(1)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
