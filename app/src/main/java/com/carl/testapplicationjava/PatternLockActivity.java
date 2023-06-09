package com.carl.testapplicationjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.carl.testapplicationjava.base.BaseActivity;

import java.util.List;

public class PatternLockActivity extends BaseActivity {

    private String firstPattern = null;
    private boolean isInitialSetup = true;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), PatternLockActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock);

        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);

    }

    private final PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));

            // 当手势绘制完成时触发
            String currentPattern = PatternLockUtils.patternToString(mPatternLockView, pattern);

            if (isInitialSetup) {
                handleInitialSetup(currentPattern);
            } else {
                handlePatternVerification(currentPattern);
            }

            // 延迟清除绘制的图案
            mPatternLockView.postDelayed(() -> mPatternLockView.clearPattern(), 1000);

        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }

    };

    private void handlePatternVerification(String currentPattern) {
        // 此处为验证阶段，可以根据实际需求进行验证逻辑的实现
        if (currentPattern.equals(firstPattern)) {
            Toast.makeText(mContext, "手势密码验证成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "手势密码错误，请重试！", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleInitialSetup(String currentPattern) {
        if (firstPattern == null) {
            firstPattern = currentPattern;
            Toast.makeText(mContext, "请再次绘制手势密码", Toast.LENGTH_SHORT).show();
        } else {
            if (firstPattern.equals(currentPattern)) {
                Toast.makeText(mContext, "手势密码设置成功！", Toast.LENGTH_SHORT).show();
                isInitialSetup = false;
            } else {
                Toast.makeText(mContext, "手势密码不匹配，请重试！", Toast.LENGTH_SHORT).show();
            }
            firstPattern = null;
        }
    }

}