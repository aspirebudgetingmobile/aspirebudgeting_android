package com.aspirebudgetingmobile.aspirebudgeting.activities;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

public class SheetsList extends AppCompatActivity {

    private static final String TAG = "SHEETS_LIST";

    ObjectFactory objectFactory = ObjectFactory.getInstance();

    RecyclerView sheetsListRecyclerView_LinkSheets;
    List<SheetsListModel> list = new ArrayList<>();
    SheetsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheets_list);

        // FETCHING ID AND SETTING UP RECYCLER VIEW
        sheetsListRecyclerView_LinkSheets = findViewById(R.id.sheetsListRecyclerView_LinkSheets);
        sheetsListRecyclerView_LinkSheets.setLayoutManager(new LinearLayoutManager(SheetsList.this, RecyclerView.VERTICAL, false));

        // GET THE GOOGLE ACCOUNT READY TO FETCH SHEETS
        objectFactory.getUserManager().getLastAccount(SheetsList.this);
        objectFactory.getUserManager().initCredential(SheetsList.this);
        objectFactory.getUserManager().initDriveService();

        // START AN ASYNC TASK TO GET THE LIST
        getSheetsList();


    }

    @SuppressLint("StaticFieldLeak")
    private void getSheetsList() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (list.size() > 0) {
                    list.clear();
                }
                list = objectFactory.getUserManager().getFiles();
                Log.e(TAG, "onCreate: " + list);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // TASK TO DO AFTER GETTING THE LIST
                showList();
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
