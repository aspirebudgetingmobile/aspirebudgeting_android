package com.aspirebudgetingmobile.aspirebudgeting.interfaces;

import javax.annotation.Nullable;

public interface BioMetricCallBack {
    void resultCallback(int result, String message, @Nullable Integer errorCode);
}
