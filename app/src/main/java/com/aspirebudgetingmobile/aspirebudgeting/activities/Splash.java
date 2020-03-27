package com.aspirebudgetingmobile.aspirebudgeting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aspirebudgetingmobile.aspirebudgeting.R;
import com.aspirebudgetingmobile.aspirebudgeting.utils.ObjectFactory;
import com.aspirebudgetingmobile.aspirebudgeting.utils.SessionConfig;
import com.aspirebudgetingmobile.aspirebudgeting.utils.UserManager;

public class Splash extends AppCompatActivity {

    ObjectFactory objectFactory = ObjectFactory.getInstance();
    UserManager userManager;
    SessionConfig sessionConfig;

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

        if (userManager.getLastAccount(Splash.this) != null) {
            // USER HAS ALREADY SIGNED IN
            if (sessionConfig.getSheetId().equals("none")){
                // USER HAS NO SHEET SELECTED
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Splash.this, SheetsList.class));
                        finish();
                    }
                }, 1500);
            } else {
                // USER HAS A SHEET SELECTED
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Splash.this, Home.class));
                        finish();
                    }
                }, 1500);
            }

        } else {
            // NEW USER OR SESSION EXPIRED
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();
                }
            }, 1500);
        }
    }
}
