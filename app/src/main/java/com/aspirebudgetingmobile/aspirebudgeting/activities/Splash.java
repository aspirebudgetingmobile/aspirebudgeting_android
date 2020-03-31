package com.aspirebudgetingmobile.aspirebudgeting.activities;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.interfaces.BioMetricCallBack;
import com.aspirebudgetingmobile.aspirebudgeting.utils.AuthenticateUser;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SessionConfig;
import com.aspirebudgetingmobile.aspirebudgeting.utils.UserManager;

import java.util.Objects;

import javax.annotation.Nullable;

public class Splash extends AppCompatActivity {

    private ObjectFactory objectFactory = ObjectFactory.getInstance();
    private UserManager userManager;
    private SessionConfig sessionConfig;
    private AuthenticateUser authenticateUser;
    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            getWindow().setNavigationBarColor(ContextCompat.getColor(Splash.this, R.color.colorPrimary));
        } catch (Exception e) {
            e.printStackTrace();
        }

        objectFactory.initContext(Splash.this);
        sessionConfig = objectFactory.getSessionConfig();
        userManager = objectFactory.getUserManager();
        authenticateUser = objectFactory.getAuthenticateUser();

        keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);

        if (Objects.requireNonNull(keyguardManager).isKeyguardSecure()){
            beginAuthorization();
        } else {
            checkUserTypeAndIntent();
        }
    }

    private void beginAuthorization() {
        // Will Generate a callback from util class when task is done
        authenticateUser.initAuthUser(new BioMetricCallBack() {
            @Override
            public void resultCallback(int i, String message, @Nullable Integer errorCode) {
                Log.e("BIOMETRIC_CALLBACK", i + "  " + message);
                switch (i) {
                    case 0:
                        // Some kind of exception occurred
                        Toast.makeText(Splash.this, "Authentication Failed ! Please try again.", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        // Auth successful
                        checkUserTypeAndIntent();
                        break;
                    case 2:
                        // Auth Failed
                        Toast.makeText(Splash.this, "Authentication Failed ! Please try again.", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        if (Objects.requireNonNull(errorCode) == 14) {
                            // No password set
                            checkUserTypeAndIntent();
                        } else if (Objects.requireNonNull(errorCode) == 10) {
                            // Authentication cancelled
                            finish();
                        }
                        break;
                }
            }
        });
        // Trigger Password screen
        authenticateUser.startAuthentication(Splash.this);
    }

    private void checkUserTypeAndIntent() {

        if (userManager.getLastAccount(Splash.this) != null) {
            // USER HAS ALREADY SIGNED IN
            if (sessionConfig.getSheetId().equals("none")) {
                // USER HAS NO SHEET SELECTED
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Splash.this, SheetsList.class));
                        finish();
                    }
                }, 1000);
            } else {
                // USER HAS A SHEET SELECTED
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Splash.this, Home.class));
                        finish();
                    }
                }, 1000);
            }

        } else {
            // NEW USER OR SESSION EXPIRED
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();
                }
            }, 1000);
        }
    }
}
