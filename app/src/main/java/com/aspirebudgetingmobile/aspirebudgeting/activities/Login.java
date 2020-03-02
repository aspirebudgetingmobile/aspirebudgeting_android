package com.aspirebudgetingmobile.aspirebudgeting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.utils.UserManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.card.MaterialCardView;

public class Login extends AppCompatActivity {

    private static final String TAG = "LOGIN_ACTIVITY";

    RelativeLayout googleRelativeLayout;
    MaterialCardView googleLoginCard_login;

    UserManager userManager;
    int GOOGLE_SIGN_IN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getWindow() != null) {
            // SETTING THE NAVIGATION BAR WHITE
            getWindow().setNavigationBarColor(ContextCompat.getColor(Login.this, android.R.color.white));
        }

        // CREATE AN OBJECT OF USER_MANAGER AND INITIALIZE GOOGLE SIGN IN
        userManager = new UserManager(Login.this, Login.this);
        userManager.initializeGoogleSignIn();

        // FETCHING IDs OF ALL ELEMENTS USED
        googleRelativeLayout = findViewById(R.id.googleLoginRelativeLayout_login);
        googleLoginCard_login = findViewById(R.id.googleLoginCard_login);

        // TRIGGER ON CLICK LISTENERS FOR ALL ELEMENTS
        onClickListeners();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // GOOGLE LOGIN SUCCESSFUL AND GIVE USER_MANAGER DATA
                userManager.setSignedInAccount(GoogleSignIn.getSignedInAccountFromIntent(data));
                handleSignInResult();
            } else {
                // IF USER CANCELS OR STOPS THE GOOGLE LOGIN PROCESS MIDWAY
                Log.e(TAG, "onActivityResult: GOOGLE LOGIN FAILED");
            }
        }
    }

    private void handleSignInResult() {
        try {
            Toast.makeText(this, "Hello, " + userManager.getName(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, Dashboard.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClickListeners() {

        googleRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GOOGLE_SIGN_IN = userManager.startGoogleLogin();
            }
        });
    }
}
