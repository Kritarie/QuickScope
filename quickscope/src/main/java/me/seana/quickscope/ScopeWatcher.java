package me.seana.quickscope;

public interface ScopeWatcher {
    void onEnterScope(Object component);
    void onExitScope(Object component);
}