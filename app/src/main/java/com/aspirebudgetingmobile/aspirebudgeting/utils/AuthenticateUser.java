package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.aspirebudgetingmobile.aspirebudgeting.interfaces.BioMetricCallBack;

import java.util.concurrent.Executor;

public class AuthenticateUser {

    private Context context;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private BioMetricCallBack bioMetric;

    AuthenticateUser(Context context) {

        this.context = context;
    }

    public void initAuthUser(BioMetricCallBack mBioMetric) {
        bioMetric = mBioMetric;

        executor = ContextCompat.getMainExecutor(context);

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Log in to your account")
                .setDeviceCredentialAllowed(true)
                .build();
    }

    public void startAuthentication(final FragmentActivity fragContext) {
        // SETTING 0 FOR EXCEPTION
        // SETTING 1 FOR SUCCESS
        // SETTING 2 FOR AUTHENTICATION FAILED
        // SETTING 3 FOR USER WITH NO AUTHENTICATION SETUP

        try {
            biometricPrompt = new BiometricPrompt(fragContext, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode,
                                                  @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Log.e("BIOMETRIC_CALLBACK",  errorCode + "");
                    bioMetric.resultCallback(3, errString.toString(), errorCode);
                }

                @Override
                public void onAuthenticationSucceeded(
                        @NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    bioMetric.resultCallback(1, "success", null);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    bioMetric.resultCallback(2, "failed", null);
                }
            });

            biometricPrompt.authenticate(promptInfo);
        } catch (Exception e){
            bioMetric.resultCallback(0, e.getMessage(), null);
        }
    }

}
