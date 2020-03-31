package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;

import java.util.concurrent.Executor;

public class AuthenticateUser {

    private Context context;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private int authResult = 0;

    public AuthenticateUser(Context context) {
        this.context = context;
    }

    public void initAuthUser() {

        executor = ContextCompat.getMainExecutor(context);

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Log in to your account")
                .setDeviceCredentialAllowed(true)
                .build();
    }

    @SuppressLint("StaticFieldLeak")
    public BiometricPrompt startAuthentication(final FragmentActivity fragContext) {
        // SETTING 0 FOR EXCEPTION
        // SETTING 1 FOR SUCCESS
        // SETTING 2 FOR AUTHENTICATION FAILED
        // SETTING 3 FOR USER WITH NO AUTHENTICATION SETUP

        biometricPrompt = new BiometricPrompt(fragContext, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(context, errString, Toast.LENGTH_SHORT).show();
                authResult = 3;
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show();
                authResult = 1;
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                authResult = 2;

            }
        });

        biometricPrompt.authenticate(promptInfo);

        return biometricPrompt;
    }

}
