package com.carl.testapplicationjava;


import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.carl.testapplicationjava.base.BaseActivity;
import com.carl.testapplicationjava.utils.FingerprintAuthHelper;
import com.carl.testapplicationjava.utils.FingerprintHandler;
import com.carl.testapplicationjava.utils.LogUtils;
import com.carl.testapplicationjava.utils.ToastUtils;

import java.util.concurrent.Executor;

public class MainActivity extends BaseActivity {

    Button btnClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnClick = findViewById(R.id.btn_click);

        judgeDeviceFingerprintManager();
        judgeDeviceBiometric();

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                authFingerprint();
                showBiometricPrompt();
            }
        });

    }

    private void authFingerprint() {
        FingerprintAuthHelper fingerprintAuthHelper = new FingerprintAuthHelper();
        FingerprintManager.CryptoObject cryptoObject = fingerprintAuthHelper.createCryptoObject();

        FingerprintHandler fingerprintHandler = new FingerprintHandler(mContext);
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, fingerprintHandler, null);
    }


    FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);

    private void judgeDeviceFingerprintManager() {
        if (!fingerprintManager.isHardwareDetected()) {
            // 设备不支持指纹识别
            LogUtils.e("设备不支持指纹识别");
        }

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            // 用户未设置指纹
        }


        if (!fingerprintManager.hasEnrolledFingerprints()) {
            // 没有注册指纹
            LogUtils.e("没有注册指纹");
        }

    }


    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric authentication")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();
        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // 处理验证错误
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                LogUtils.e("onAuthenticationSucceeded," + result.toString());
                ToastUtils.showShort("指纹认证成功");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // 验证失败的操作
            }
        });

        biometricPrompt.authenticate(promptInfo);

    }


    BiometricManager biometricManager = BiometricManager.from(this);

    public void judgeDeviceBiometric() {
        int result = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK);
        if (result == BiometricManager.BIOMETRIC_SUCCESS) {
            // 可以进行生物识别认证
            showBiometricPrompt();
        } else {
            // 无法进行生物识别认证
        }

//        switch (result) {
//            case BiometricManager.BIOMETRIC_SUCCESS:
//                // 设备支持生物识别
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                // 设备不支持生物识别
//                LogUtils.e("设备不支持指纹识别");
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                // 当前生物识别硬件不可用
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                // 用户尚未设置生物识别
//                break;
//        }

    }

}