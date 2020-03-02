package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class UserManager {

    private Context context;
    private Activity activity;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private Task<GoogleSignInAccount> task;
    private GoogleSignInAccount account;
    private static final int GOOGLE_SIGN_IN = 10;

    SessionConfig sessionConfig;

    public UserManager(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        sessionConfig = new SessionConfig(context);
    }

    public void initializeGoogleSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/drive.file"), new Scope("https://www.googleapis.com/auth/spreadsheets"))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public int startGoogleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN);

        return GOOGLE_SIGN_IN;
    }

    public void setSignedInAccount(Task<GoogleSignInAccount> task) {
        if (task != null){
            this.task = task;
            account = task.getResult();
            if (account != null) {
                // SET ALL THE DATA IN LOCAL SHARED PREFERENCE SO THAT WE CAN ACCESS IT ACROSS APPLICATION
                sessionConfig.setEmail(account.getEmail());
                sessionConfig.setName(account.getDisplayName());
                sessionConfig.setProfilePic(Objects.requireNonNull(account.getPhotoUrl()).toString());
                sessionConfig.setloginStatus(true);
            }
        }
    }

    public GoogleSignInAccount getSignedInAccount() {
        if (account == null){
            throw new NullPointerException("No account available");
        } else {
            return account;
        }

    }

    public String getName() {
        if (account == null) {
            return "null";
        } else {
            return account.getDisplayName();
        }
    }

    public String getEmail() {
        if (account == null) {
            return "null";
        } else {
            return account.getEmail();
        }

    }

    public String getProfilePic() {
        if (account == null) {
            return "null";
        } else {
            return Objects.requireNonNull(account.getPhotoUrl()).toString();
        }

    }
}
