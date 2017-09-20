package me.seana.quickscope.sample;

import android.util.Log;

import me.seana.quickscope.ScopeWatcher;

public class LoggingScopeWatcher implements ScopeWatcher {

    private static final String TAG = "LoggingScopeWatcher";

    @Override
    public void onEnterScope(Object component) {
        Log.d(TAG, "Enter: " + component);
    }

    @Override
    public void onExitScope(Object component) {
        Log.d(TAG, "Exit: " + component);
    }
}