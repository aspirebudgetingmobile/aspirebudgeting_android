package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.aspirebudgetingmobile.aspirebudgeting.models.SheetsListModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserManager {

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private Task<GoogleSignInAccount> task;
    private GoogleSignInAccount account;
    private GoogleAccountCredential credential;
    private Drive driveService;
    private List<SheetsListModel> sheetList = new ArrayList<>();
    private static AsyncTask<Void, Void, String> listTask;

    private static final int GOOGLE_SIGN_IN = 10;
    private static final String TAG = "USER_MANAGER";

    SessionConfig sessionConfig;

    public UserManager() {
    }

    public void initializeGoogleSignIn(Context context) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/drive.file"), new Scope("https://www.googleapis.com/auth/spreadsheets"))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public int startGoogleLogin(Activity activity) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN);

        return GOOGLE_SIGN_IN;
    }

    public void setSignedInAccount(Task<GoogleSignInAccount> task, Context context) {
        if (task != null) {
            this.task = task;
            account = task.getResult();
            if (account != null) {
                // SET ALL THE DATA IN LOCAL SHARED PREFERENCE SO THAT WE CAN ACCESS IT ACROSS APPLICATION
                sessionConfig = new SessionConfig(context);
                sessionConfig.setEmail(account.getEmail());
                sessionConfig.setName(account.getDisplayName());
                sessionConfig.setProfilePic(Objects.requireNonNull(account.getPhotoUrl()).toString());
                sessionConfig.setloginStatus(true);
            }
        }
    }

    public GoogleSignInAccount getSignedInAccount() {
        if (account == null) {
            throw new NullPointerException("No account available");
        } else {
            return account;
        }

    }

    public String getName() {
        if (account == null) {
            return "null";
        } else {
            return account.getDisplayName();
        }
    }

    public String getEmail() {
        if (account == null) {
            return "null";
        } else {
            return account.getEmail();
        }

    }

    public String getProfilePic() {
        if (account == null) {
            return "null";
        } else {
            return Objects.requireNonNull(account.getPhotoUrl()).toString();
        }
    }

    public GoogleSignInAccount getLastAccount(Context context) {
        if (GoogleSignIn.getLastSignedInAccount(context) != null)
            account = GoogleSignIn.getLastSignedInAccount(context);
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public int initCredential(Context context) {
        if (account != null) {
            credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(account.getAccount());
            return 1;
        } else {
            return 0;
        }
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void initDriveService() {
        driveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(),
                new GsonFactory(), credential)
                .build();
    }


    public List<SheetsListModel> getFiles() {

        Drive.Files.List request = null;
        if (sheetList.size() > 0){
            sheetList.clear();
        }
        try {
            request = driveService.files().list()
                    .setPageSize(10)
                    .setQ("mimeType = 'application/vnd.google-apps.spreadsheet'")
                    .setFields("nextPageToken, files(id, name)");


            FileList result = request.execute();

            List<File> files = result.getFiles();

            String spreadsheetId = null;

            if (files != null) {
                for (File file : files) {

                    Log.e(TAG, "doInBackground:XXX " + file.getName());
                    spreadsheetId = file.getId();

                    sheetList.add(new SheetsListModel(file.getId(), file.getName()));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return sheetList;
    }
}
