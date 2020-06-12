package com.aspirebudgetingmobile.aspirebudgeting.fragments;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.activities.Home;
import com.aspirebudgetingmobile.aspirebudgeting.adapters.DashboardCardsAdapter;
import com.aspirebudgetingmobile.aspirebudgeting.models.DashboardCardsModel;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SessionConfig;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SheetsManager;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Fragment {

    private static final String TAG = "DASHBOARD";

    private View view;
    private Context context;
    private SheetsManager sheetsManager;
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private SessionConfig sessionConfig;
    private String sheetID = "";
    private boolean isErrorCaused = false;

    private List<DashboardCardsModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private DashboardCardsAdapter adapter;

    private LinearLayout loadingLayout;
    private ImageView aspireLogoDashboard;
    private SwipeRefreshLayout swipeRefresh_dashboard;
    private ProgressBar loadingProgress;
    private TextView loadingText;
    private Home home;

    public Dashboard(Home home) {
        this.home = home;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = view.getContext();

        // FETCH AND INITIALIZE THE VIEWS AND CLASSES
        fetchIDs_init();
        // INIT LOADING ANIMATION
        loadingAnimation();
        // FETCH CATEGORIES AND GROUPS TO DISPLAY
        getGroupToDisplay();
        // ON CLICK LISTENERS
        onClickListeners();

        return view;
    }

    private void onClickListeners() {
        swipeRefresh_dashboard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGroupToDisplay();
            }
        });
    }

    private void fetchIDs_init() {
        aspireLogoDashboard = view.findViewById(R.id.aspireLogoDashboard);
        swipeRefresh_dashboard = view.findViewById(R.id.swipeRefresh_dashboard);
        swipeRefresh_dashboard.setRefreshing(true);

        // INITIALIZE ALL THE UTIL CLASSES
        sessionConfig = objectFactory.getSessionConfig();
        sheetsManager = objectFactory.getSheetsManager();

        // FETCH THE SHEET ID FROM SESSION
        sheetID = sessionConfig.getSheetId();

        // FETCH ID OF VIEWS IN LAYOUT
        loadingLayout = view.findViewById(R.id.loadingLayout);
        loadingProgress = view.findViewById(R.id.loadingProgress);
        loadingText = view.findViewById(R.id.loadingText);

        recyclerView = view.findViewById(R.id.dashboardCardsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    private void loadingAnimation() {
        // FADE IN FADE OUT ANIMATION
        Animation connectingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_infinite);
        aspireLogoDashboard.startAnimation(connectingAnimation);

        // DRAWABLE CHANGING ANIMATION
        ValueAnimator anim = ValueAnimator.ofInt(0, 1, 2, 3, 4, 5, 6, 7);
        anim.setDuration(5000);


        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                // DEVELOPING FADE IN FADE OUT ANIMATION
                switch ((Integer) animation.getAnimatedValue()) {
                    case 0:
                        aspireLogoDashboard.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dollar_white_icon));
                        break;
                    case 2:
                        aspireLogoDashboard.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.inr_white_icon));
                        break;
                    case 4:
                        aspireLogoDashboard.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.pound_white_icon));
                        break;
                    case 6:
                        aspireLogoDashboard.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.yen_white_icon));
                        break;
                }

            }
        });

        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);

        anim.start();
    }

    @SuppressLint("StaticFieldLeak")
    private void getGroupToDisplay() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    list = sheetsManager.fetchCategoriesAndGroups(context, sheetID);
                    isErrorCaused = false;
                } catch (Exception e) {
                    isErrorCaused = true;
                    Log.e(TAG, "doInBackground: " + e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isErrorCaused){
                    loadingText.setText("There was some problem with permission, please logout and try again.");
                    loadingProgress.setVisibility(View.GONE);
                    swipeRefresh_dashboard.setRefreshing(false);
                } else {
                    adapter = new DashboardCardsAdapter(context, list);
                    recyclerView.setAdapter(adapter);
                    loadingLayout.setVisibility(View.GONE);
                    swipeRefresh_dashboard.setRefreshing(false);
                }
                home.dataLoaded();
            }
        }.execute();
    }

    public void reloadCards() {
        getGroupToDisplay();
    }
}


