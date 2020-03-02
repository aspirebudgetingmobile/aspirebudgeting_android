package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionConfig {

    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String initSharedPref = "com.aspirebudgetingmobile.aspirebudgeting_init";
    private static final String SharedPref_loginStatus = "com.aspirebudgetingmobile.aspirebudgeting_loginStatus";
    private static final String SharedPref_email = "com.aspirebudgetingmobile.aspirebudgeting_email";
    private static final String SharedPref_name = "com.aspirebudgetingmobile.aspirebudgeting_name";
    private static final String SharedPref_profile = "com.aspirebudgetingmobile.aspirebudgeting_profile";

    public SessionConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(initSharedPref, Context.MODE_PRIVATE);
    }

    public void setloginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SharedPref_loginStatus, status);
        editor.apply();
    }

    public boolean getloginStatus() {
        return sharedPreferences.getBoolean(SharedPref_loginStatus, false);
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPref_email, email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(SharedPref_email, "none");
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPref_name, name);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString(SharedPref_name, "none");
    }

    public void setProfilePic(String profilePic) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPref_profile, profilePic);
        editor.apply();
    }

    public String getProfilePic() {
        return sharedPreferences.getString(SharedPref_profile, "none");
    }

}
