package com.aspirebudgetingmobile.aspirebudgeting.models;

import androidx.annotation.Nullable;

public class AccountBalanceModel {
    String accountName;
    String amount;
    String lastUpdatedOn;

    public AccountBalanceModel(String accountName, String amount, @Nullable String lastUpdatedOn) {
        this.accountName = accountName;
        this.amount = amount;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}

