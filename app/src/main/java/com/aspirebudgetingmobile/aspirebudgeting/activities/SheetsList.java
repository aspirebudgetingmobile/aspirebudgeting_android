package com.aspirebudgetingmobile.aspirebudgeting.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.adapters.SheetsListAdapter;
import com.aspirebudgetingmobile.aspirebudgeting.models.SheetsListModel;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SessionConfig;
import com.aspirebudgetingmobile.aspirebudgeting.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

public class SheetsList extends AppCompatActivity {

    private static final String TAG = "SHEETS_LIST";

    ObjectFactory objectFactory = ObjectFactory.getInstance();
    UserManager userManager;
    SessionConfig sessionConfig;

    RecyclerView sheetsListRecyclerView_LinkSheets;
    List<SheetsListModel> list = new ArrayList<>();
    SheetsListAdapter adapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheets_list);

        // FETCHING ID AND SETTING UP RECYCLER VIEW
        sheetsListRecyclerView_LinkSheets = findViewById(R.id.sheetsListRecyclerView_LinkSheets);
        sheetsListRecyclerView_LinkSheets.setLayoutManager(new LinearLayoutManager(SheetsList.this, RecyclerView.VERTICAL, false));

        // INITIALIZE USER MANAGER AND SESSION CONFIG
        userManager = objectFactory.getUserManager();
        sessionConfig = new SessionConfig(SheetsList.this);

        // IF USER HAS SELECTED THE SHEET BEFORE, SKIP THIS SCREEN
        if (!sessionConfig.getSheetId().equals("none")){
            startActivity(new Intent(SheetsList.this, Home.class));
            finish();
        } else {

            // GET THE GOOGLE ACCOUNT READY TO FETCH SHEETS
            userManager.getLastAccount(SheetsList.this);
            userManager.initCredential(SheetsList.this);
            userManager.initDriveService();

            // INITIALIZE PROGRESS DIALOG
            progressDialog = new ProgressDialog(SheetsList.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            // START AN ASYNC TASK TO GET THE LIST
            getSheetsList();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getSheetsList() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (list.size() > 0) {
                    list.clear();
                }
                list = userManager.getFiles();
                Log.e(TAG, "onCreate: " + list);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // TASK TO DO AFTER GETTING THE LIST
                showList();

                // DISMISS PROGRESS DIALOG
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }.execute();


    }

    private void showList() {
        // INITIALIZE ADAPTER
        adapter = new SheetsListAdapter(SheetsList.this, list);
        // POPULATE THE LIST
        sheetsListRecyclerView_LinkSheets.setAdapter(adapter);
    }
}
