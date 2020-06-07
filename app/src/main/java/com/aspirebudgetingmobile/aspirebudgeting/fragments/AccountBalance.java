package com.aspirebudgetingmobile.aspirebudgeting.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.adapters.AccountBalanceAdapter;
import com.aspirebudgetingmobile.aspirebudgeting.models.AccountBalanceModel;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SessionConfig;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SheetsManager;

import java.util.ArrayList;
import java.util.List;

public class AccountBalance extends Fragment {

    private View view;
    private Context context;
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private SessionConfig sessionConfig;
    private SheetsManager sheetsManager;
    private boolean isErrorCaused = false;
    private List<AccountBalanceModel> list = new ArrayList<>();
    private AccountBalanceAdapter adapter;

    private RecyclerView accountBalanceRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout loadingLayout;
    private ProgressBar loadingProgress;
    private TextView loadingText;

    public AccountBalance() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_balance, container, false);
        context = view.getContext();

        initView();
        onClickListeners();
        fetchAccountBalance();
        return view;
    }

    private void initView() {

        sessionConfig = objectFactory.getSessionConfig();
        sheetsManager = objectFactory.getSheetsManager();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh_accountBalance);
        swipeRefreshLayout.setRefreshing(true);

        loadingLayout = view.findViewById(R.id.loadingLayout);
        loadingProgress = view.findViewById(R.id.loadingProgress);
        loadingText = view.findViewById(R.id.loadingText);
        accountBalanceRecyclerView = view.findViewById(R.id.accountBalanceRecyclerView);
        accountBalanceRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

    }

    private void onClickListeners() {

    }

    @SuppressLint("StaticFieldLeak")
    private void fetchAccountBalance() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    list = sheetsManager.fetchAccountBalance(context, sessionConfig.getSheetId());
                    isErrorCaused = false;
                } catch (Exception e) {
                    isErrorCaused = true;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isErrorCaused) {
                    loadingText.setText("There was some problem with permission, please logout and try again.");
                    loadingProgress.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    adapter = new AccountBalanceAdapter(context, list);
                    accountBalanceRecyclerView.setAdapter(adapter);
                    loadingLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }.execute();
    }
}
