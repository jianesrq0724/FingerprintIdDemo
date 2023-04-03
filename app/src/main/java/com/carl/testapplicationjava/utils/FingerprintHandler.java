package com.carl.testapplicationjava.utils;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        // 处理认证错误
        LogUtils.e("onAuthenticationError," + errMsgId + ", " + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        // 提供帮助信息
        LogUtils.e("onAuthenticationHelp," + helpMsgId + ", " + helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        // 认证成功的操作
        LogUtils.e("onAuthenticationSucceeded," + result.toString());
        ToastUtils.showShort("指纹认证成功");
    }

    @Override
    public void onAuthenticationFailed() {
        // 认证失败的操作
        LogUtils.e("onAuthenticationFailed");
    }

}
