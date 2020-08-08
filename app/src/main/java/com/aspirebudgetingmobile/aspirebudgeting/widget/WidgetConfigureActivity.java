package com.aspirebudgetingmobile.aspirebudgeting.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.adapters.WidgetConfigCheckboxAdapter;
import com.aspirebudgetingmobile.aspirebudgeting.models.DashboardCardsModel;
import com.aspirebudgetingmobile.aspirebudgeting.models.WidgetCategoriesModel;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SessionConfig;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SheetsManager;
import com.aspirebudgetingmobile.aspirebudgeting.utils.UserManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * The configuration screen for the {@link Widget DashboardWidget} AppWidget.
 */
public class WidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.aspirebudgetingmobile.aspirebudgeting.DashboardWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static SessionConfig sessionConfig;
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private UserManager userManager;
    private boolean isValidSheet;
    private View loadingLayout;

    private SheetsManager sheetsManager;
    private String sheetID = "";
    private WidgetConfigCheckboxAdapter adapter;
    private RecyclerView recyclerView;
    private List<DashboardCardsModel> list = new ArrayList<>();
    private List<WidgetCategoriesModel> categoryList = new ArrayList<>();
    private List<String> categories = new ArrayList<String>();
    private List<String> selectedCategories = new ArrayList<String>();

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Context context;


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetConfigureActivity.this;
            //Save the selected categories
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getSelected()) {
                    selectedCategories.add(categoryList.get(i).getCategoryName());
                }
            }
            saveCategoryList(context, mAppWidgetId, selectedCategories);
            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Widget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public WidgetConfigureActivity() {
        super();
    }

    void saveCategoryList(Context context, int appWidgetId, List<String> categories) {
        sessionConfig.setWidgetCategoryList(Integer.toString(appWidgetId),categories);
    }

    public static List<String> loadCategoryList(Context context, int appWidgetId) {
        return sessionConfig.getWidgetCategoryList(Integer.toString(appWidgetId));
    }

    public static void deletePref(Context context, int appWidgetId) {
        sessionConfig.removeWidgetCategoryList(Integer.toString(appWidgetId));
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.widget_config);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        context = WidgetConfigureActivity.this;
        objectFactory.initContext(WidgetConfigureActivity.this);
        sessionConfig = objectFactory.getSessionConfig();

        loadingLayout = findViewById(R.id.loadingLayoutWidget);
        userManager = objectFactory.getUserManager();
        isValidSheet = checkUserTypeAndIntent(1500);
        if (isValidSheet) {
            sheetsManager = objectFactory.getSheetsManager();
            // FETCH THE SHEET ID FROM SESSION
            sheetID = sessionConfig.getSheetId();
            // FETCH CATEGORIES AND GROUPS TO DISPLAY
            recyclerView = findViewById(R.id.categoriesCheckboxRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            recyclerView.setHasFixedSize(true);
            getCategories();
        }


    }

    private boolean checkUserTypeAndIntent(long delay) {

        if (userManager.getLastAccount(WidgetConfigureActivity.this) != null) {
            // USER HAS ALREADY SIGNED IN
            if (sessionConfig.getSheetId().equals("none")) {
                // USER HAS NO SHEET SELECTED
                Log.e("checkUser", "checkUserTypeAndIntent: Please select a sheet in app");
            } else {
                // USER HAS A SHEET SELECTED
                return true;
            }
        } else {
            // NEW USER OR SESSION EXPIRED
            Log.e("checkUser", "checkUserTypeAndIntent: Please select a login in app");
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private void getCategories() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                //Bug? If there's no internet connection, app just closes
                list = sheetsManager.fetchCategoriesAndGroups(context, sheetID);
                for (int i = 0; i < list.size(); i++) {
                    categories.addAll(list.get(i).getCategoryName());
                }
                for (int i = 0; i < categories.size(); i++) {
                    categoryList.add(new WidgetCategoriesModel(categories.get(i)));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//              Populate the Checkbox List adapter
                adapter = new WidgetConfigCheckboxAdapter(context, categoryList);
                recyclerView.setAdapter(adapter);
                loadingLayout.setVisibility(View.GONE);
            }

        }.execute();
    }
}

