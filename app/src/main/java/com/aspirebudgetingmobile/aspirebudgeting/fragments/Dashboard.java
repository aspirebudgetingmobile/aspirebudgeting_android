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
    private List<List<Object>> fetchedData = null;
    private JSONArray parsedData = new JSONArray();
    private List<Object> fakeBuffer = new ArrayList<>();

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
        adapter = new DashboardCardsAdapter(context, list);

        // CREATED A FAKE LIST ITEM TO INCLUDE THE LAST ITEM IN MAIN DATA
        fakeBuffer.add("FakeBuffer");

        // FETCH CATEGORIES AND GROUPS TO DISPLAY
        getGroupToDisplay();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void getGroupToDisplay() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                fetchedData = sheetsManager.fetchCategoriesAndGroups(context, sheetID);
                fetchedData.add(fakeBuffer);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                parseFetchedData();
            }
        }.execute();
    }

    private void parseFetchedData() {
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
            recyclerView.setAdapter(adapter);
            loadingLayout.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


