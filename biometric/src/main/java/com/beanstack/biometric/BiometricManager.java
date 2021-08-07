package com.beanstack.biometric;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class BiometricManager extends BiometricAuthenticateManager {

    protected CancellationSignal mCancellationSignal = new CancellationSignal();
    private final String title;
    private final String subtitle;
    private final String description;
    private final String negativeButtonText;
    private final Context context;

    protected BiometricManager(final BiometricBuilder biometricBuilder) {
        this.context = biometricBuilder.context;
        this.title = biometricBuilder.title;
        this.subtitle = biometricBuilder.subtitle;
        this.description = biometricBuilder.description;
        this.negativeButtonText = biometricBuilder.negativeButtonText;
    }


    @RequiresApi(api = VERSION_CODES.M)
    public void authenticate(@NonNull final BiometricCallback biometricCallback) {
        if (title == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog title cannot be null");
            return;
        }
        if (subtitle == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog subtitle cannot be null");
            return;
        }
        if (description == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog description cannot be null");
            return;
        }
        if (negativeButtonText == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog negative button text cannot be null");
            return;
        }
        if (!BiometricUtils.isSdkVersionSupported()) {
            biometricCallback.onSdkVersionNotSupported();
            return;
        }
        if (!BiometricUtils.isPermissionGranted(context)) {
            biometricCallback.onBiometricAuthenticationPermissionNotGranted();
            return;
        }
        if (!BiometricUtils.isHardwareSupported(context)) {
            biometricCallback.onBiometricAuthenticationNotSupported();
            return;
        }
        if (!BiometricUtils.isFingerprintAvailable(context)) {
            biometricCallback.onBiometricAuthenticationNotAvailable();
            return;
        }
        displayBiometricDialog(biometricCallback);
    }

    @RequiresApi(api = VERSION_CODES.M)
    public void cancelAuthentication() {
        if (BiometricUtils.isBiometricPromptEnabled()) {
            if (!mCancellationSignal.isCanceled())
                mCancellationSignal.cancel();
        } else {
            if (!mCancellationSignalV23.isCanceled())
                mCancellationSignalV23.cancel();
        }
    }


    @RequiresApi(api = VERSION_CODES.M)
    private void displayBiometricDialog(BiometricCallback biometricCallback) {
        if (BiometricUtils.isBiometricPromptEnabled()) {
            displayBiometricPrompt(biometricCallback);
        } else {
            displayAuthMgrBiometricPrompt(biometricCallback);
        }
    }


    @TargetApi(Build.VERSION_CODES.P)
    private void displayBiometricPrompt(final BiometricCallback biometricCallback) {
        new BiometricPrompt.Builder(context)
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButton(negativeButtonText, context.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        biometricCallback.onAuthenticationCancelled();
                    }
                })
                .build()
                .authenticate(mCancellationSignal, context.getMainExecutor(),
                        new BiometricCallbackImpl(biometricCallback));
    }


    public static class BiometricBuilder {

        private String title;
        private String subtitle;
        private String description;
        private String negativeButtonText;

        private final Context context;

        public BiometricBuilder(Context context) {
            this.context = context;
        }

        public BiometricBuilder setTitle(@NonNull final String title) {
            this.title = title;
            return this;
        }

        public BiometricBuilder setSubtitle(@NonNull final String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public BiometricBuilder setDescription(@NonNull final String description) {
            this.description = description;
            return this;
        }


        public BiometricBuilder setNegativeButtonText(@NonNull final String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public BiometricManager build() {
            return new BiometricManager(this);
        }
    }
}
