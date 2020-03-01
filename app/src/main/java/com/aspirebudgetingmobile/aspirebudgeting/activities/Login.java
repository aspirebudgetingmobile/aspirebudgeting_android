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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;

public class Login extends AppCompatActivity {

    private static final String TAG = "LOGIN_ACTIVITY";

    RelativeLayout googleRelativeLayout;
    MaterialCardView googleLoginCard_login;

    private static final int GOOGLE_SIGN_IN = 10;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getWindow() != null) {
            // SETTING THE NAVIGATION BAR WHITE
            getWindow().setNavigationBarColor(ContextCompat.getColor(Login.this, android.R.color.white));
        }

        //SETTING UP GOOGLE SIGN IN OPTIONS AND CLIENT WITH DRIVE AND SHEETS SCOPE
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/drive.file"), new Scope("https://www.googleapis.com/auth/spreadsheets"))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // FETCHING IDs OF ALL ELEMENTS USED
        googleRelativeLayout = findViewById(R.id.googleLoginRelativeLayout_login);
        googleLoginCard_login = findViewById(R.id.googleLoginCard_login);

        // TRIGGER ON CLICK LISTENERS FOR ALL ELEMENTS
        onClickListeners();
    }

    /**
     * METHOD TO TRIGGER GOOGLE LOGIN DIALOG
     */
    public void googleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // GOOGLE LOGIN SUCCESSFUL
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            } else {
                Log.e(TAG, "onActivityResult: GOOGLE LOGIN FAILED");
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                Toast.makeText(this, "Hello, " + account.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(Login.this, Dashboard.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClickListeners() {

        googleRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogin();
            }
        });
    }
}
