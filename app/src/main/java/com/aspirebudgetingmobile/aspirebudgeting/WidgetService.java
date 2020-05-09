package com.aspirebudgetingmobile.aspirebudgeting;


import android.content.Intent;
import android.widget.RemoteViewsService;

import com.aspirebudgetingmobile.aspirebudgeting.adapters.WidgetDataProvider;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
