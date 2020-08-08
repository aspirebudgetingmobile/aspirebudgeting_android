package com.aspirebudgetingmobile.aspirebudgeting.adapters;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.widget.WidgetConfigureActivity;
import com.aspirebudgetingmobile.aspirebudgeting.models.DashboardCardsModel;
import com.aspirebudgetingmobile.aspirebudgeting.models.WidgetCategoriesModel;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SessionConfig;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SheetsManager;
import com.aspirebudgetingmobile.aspirebudgeting.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private List<String> savedCategories = new ArrayList<>();
    private List<String> categoryNames= new ArrayList<>();
    private List<String> spentAmounts= new ArrayList<>();
    private List<String> budgetedAmounts= new ArrayList<>();
    private List<String> availableAmounts= new ArrayList<>();
    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private UserManager userManager;
    private SessionConfig sessionConfig;
    private List<WidgetCategoriesModel> categoryList = new ArrayList<>();
    private Context context;
    private int appWidgetId;

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = Objects.requireNonNull(intent.getExtras()).getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        categoryList.add(new  WidgetCategoriesModel("Loading...","0","0","0"));
    }

    @Override
    public void onDataSetChanged() {
        categoryList.clear();
        // If they gave us an intent without the widget id, just bail.
        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            savedCategories = WidgetConfigureActivity.loadCategoryList(context, appWidgetId);
            objectFactory.initContext(context);
            sessionConfig = objectFactory.getSessionConfig();
            userManager = objectFactory.getUserManager();
            boolean isValidSheet = checkUserTypeAndIntent(1500);
            if (isValidSheet) {
                SheetsManager sheetsManager = objectFactory.getSheetsManager();
                // FETCH THE SHEET ID FROM SESSION
                String sheetID = sessionConfig.getSheetId();
                List<DashboardCardsModel> list = sheetsManager.fetchCategoriesAndGroups(context, sheetID);
                for (int i = 0; list != null && i < list.size(); i++) {
                    categoryNames.addAll(list.get(i).getCategoryName());
                    spentAmounts.addAll(list.get(i).getSpentAmount());
                    budgetedAmounts.addAll(list.get(i).getBudgetedAmount());
                    availableAmounts.addAll(list.get(i).getAvailableAmount());
                }

                for (int i = 0; i < categoryNames.size(); i++) {
                    if (savedCategories.contains(categoryNames.get(i))) {
                        categoryList.add(new WidgetCategoriesModel
                                (categoryNames.get(i), budgetedAmounts.get(i), spentAmounts.get(i), availableAmounts.get(i)));
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(context.getPackageName(),
                R.layout.widget_row);
        view.setTextViewText(R.id.widget_row_catagoryName, categoryList.get(position).getCategoryName());
        view.setTextViewText(R.id.widget_row_spentAmount, categoryList.get(position).getSpentAmount());
        view.setTextViewText(R.id.widget_row_availableAmount, categoryList.get(position).getAvailableAmount());
        view.setTextViewText(R.id.widget_row_budgetedAmount, categoryList.get(position).getBudgetedAmount());
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private boolean checkUserTypeAndIntent(long delay) {

        if (userManager.getLastAccount(context) != null) {
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
}
