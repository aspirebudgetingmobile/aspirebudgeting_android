package com.aspirebudgetingmobile.aspirebudgeting.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.adapters.DashboardCardsAdapter;
import com.aspirebudgetingmobile.aspirebudgeting.models.DashboardCardsModel;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SessionConfig;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SheetsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dashboard extends Fragment {

    private static final String TAG = "DASHBOARD";

    private View view;
    private Context context;
    private SheetsManager sheetsManager;
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private SessionConfig sessionConfig;
    private String sheetID = "";

    private List<DashboardCardsModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private DashboardCardsAdapter adapter;

    LinearLayout loadingLayout;

    public Dashboard() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = view.getContext();

        // INITIALIZE ALL THE UTIL CLASSES
        sessionConfig = objectFactory.getSessionConfig();
        sheetsManager = objectFactory.getSheetsManager();

        // FETCH THE SHEET ID FROM SESSION
        sheetID = sessionConfig.getSheetId();

        // FETCH ID OF VIEWS IN LAYOUT
        loadingLayout = view.findViewById(R.id.loadingLayout);

        recyclerView = view.findViewById(R.id.dashboardCardsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        // FETCH CATEGORIES AND GROUPS TO DISPLAY
        getGroupToDisplay();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void getGroupToDisplay() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                list = sheetsManager.fetchCategoriesAndGroups(context, sheetID);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                adapter = new DashboardCardsAdapter(context, list);
                recyclerView.setAdapter(adapter);
                loadingLayout.setVisibility(View.GONE);
            }
        }.execute();
    }

}


