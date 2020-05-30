package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.content.Context;
import android.util.Log;

import com.aspirebudgetingmobile.aspirebudgeting.interfaces.TransactionCallBack;
import com.aspirebudgetingmobile.aspirebudgeting.models.DashboardCardsModel;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SheetsManager {

    // Objects of classes
    private Sheets service;
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private UserManager userManager;
    private SessionConfig sessionConfig;
    private GoogleAccountCredential credential;

    // Constants
    private static final String twoEight = "2.8";
    private static final String three = "3.0";
    private static final String threeOne = "3.1.0";
    private static final String threeTwo = "3.2.0";
    private static final String appendTransactionRange = "Transactions!B:H";

    // Util Data
    private List<String> transactionCategories = new ArrayList<>();
    private List<String> transactionAccounts = new ArrayList<>();

    public SheetsManager() {
        userManager = objectFactory.getUserManager();
        sessionConfig = objectFactory.getSessionConfig();
    }

    private List<List<Object>> fetchData(Context context, String sheetID, String range) {

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

        return values;
    }

    public List<DashboardCardsModel> fetchCategoriesAndGroups(Context context, String sheetID) {
        getTransactionCategories(context, sheetID);
        getTransactionAccounts(context, sheetID);
        verifySheet(context, sheetID);
        return parseFetchedData(fetchData(context, sheetID, "Dashboard!H4:O"));
    }

    private List<DashboardCardsModel> parseFetchedData(List<List<Object>> fetchedData) {

        List<Object> fakeBuffer = new ArrayList<>();
        fakeBuffer.add("FakeBuffer");
        Objects.requireNonNull(fetchedData).add(fakeBuffer);

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

                    name = test.toString().replace("[", "").replace("]", "");
                    innerObject.put("name", name);

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

    public boolean verifySheet(Context context, String sheetID) {
        try {
            List<Object> parentObject = fetchData(context, sheetID, "BackendData!2:2").get(0);
            String version = parentObject.get(parentObject.size() - 1).toString();
            return isSheetVersionSupported(version);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isSheetVersionSupported(String version) {

        List<String> supportedVersion = new ArrayList<>();
        supportedVersion.add(threeOne);
        supportedVersion.add(three);
        supportedVersion.add(twoEight);

        sessionConfig.setSheetVersion(version);

        return supportedVersion.contains(version);
    }

    private void getTransactionCategories(Context context, String sheetID) {
        String range = "";

        switch (sessionConfig.getSheetVersion()) {
            case twoEight:
                range = "BackendData!B2:B";
                break;
            case three:
            case threeOne:
                range = "BackendData!F2:F";
                break;
            case threeTwo:
                range = "BackendData!G2:G";
                break;
        }

        List<List<Object>> data = fetchData(context, sheetID, range);
        transactionCategories = convertToOneDimension(data);
    }

    private void getTransactionAccounts(Context context, String sheetID) {
        String range = "";

        switch (sessionConfig.getSheetVersion()) {
            case twoEight:
                range = "BackendData!E2:E";
                break;
            case three:
                range = "BackendData!H2:H";
                break;
            case threeOne:
                range = "BackendData!J2:J";
                break;
            case threeTwo:
                range = "BackendData!M2:M";
                break;
        }

        List<List<Object>> data = fetchData(context, sheetID, range);
        transactionAccounts = convertToOneDimension(data);
    }

    private List<String> convertToOneDimension(List<List<Object>> data) {
        List<String> result = new ArrayList<>();
        for (int i=0; i< data.size(); i++){
            result.add(String.valueOf(data.get(i)).replace("[", "").replace("]", ""));
        }
        return result;
    }

    public void addTransaction(String amount, String memo, String date, String category, String account,
                               int transactionType, int approvalType, TransactionCallBack transactionCallBack) {

        try {
            AppendValuesResponse appendTransaction =
                    service.spreadsheets().values().append(sessionConfig.getSheetId(),
                            appendTransactionRange, createSheetsValueRangeFrom(amount, memo, date, category, account, transactionType, approvalType))
                    .setValueInputOption("RAW")
                    .execute();

            if (appendTransaction.getUpdates().getUpdatedRange() !=null &&
                    !appendTransaction.getUpdates().getUpdatedRange().isEmpty()){
                transactionCallBack.onSuccess();
            } else {
                transactionCallBack.onError();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private ValueRange createSheetsValueRangeFrom(String amount, String memo, String date, String category,
                                                  String account, int transactionType, int approvalType) {

        ValueRange sheetsValueRange = new ValueRange();

        List<Object> valuesToInsert = new ArrayList<>();

        valuesToInsert.add(date);

        if (transactionType == 0) {
            valuesToInsert.add("");
            valuesToInsert.add(amount);
        } else {
            valuesToInsert.add(amount);
            valuesToInsert.add("");
        }

        valuesToInsert.add(category);
        valuesToInsert.add(account);
        valuesToInsert.add(String.format("(%s) - Added from Aspire Android app", memo));

        switch (sessionConfig.getSheetVersion()) {
            case twoEight:
                if (approvalType == 0){
                    valuesToInsert.add("\uD83C\uDD97");
                } else {
                    valuesToInsert.add("\u23FA");
                }
                break;
            case three:
            case threeOne:
            case threeTwo:
                if (approvalType == 0){
                    valuesToInsert.add("✅");
                } else {
                    valuesToInsert.add("\uD83C\uDD7F️");
                }
                break;
        }
        sheetsValueRange.setValues(convertTo2Dimension(valuesToInsert));
        return sheetsValueRange;
    }

    private List<List<Object>> convertTo2Dimension(List<Object> value){
        List<List<Object>> result = new ArrayList<>();
        result.add(0, value);
        return result;
    }

    public List<String> getTransactionCategories(){
        return transactionCategories;
    }

    public List<String> getTransactionAccounts(){
        return transactionAccounts;
    }
}
