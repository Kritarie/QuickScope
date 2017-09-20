package me.seana.quickscope;

import java.util.List;

class AggregateScopeWatcher implements ScopeWatcher {

    private final List<ScopeWatcher> watchers;

    AggregateScopeWatcher(List<ScopeWatcher> watchers) {
        this.watchers = watchers;
    }

    @Override
    public void onEnterScope(Object component) {
        for(ScopeWatcher w : watchers) {
            w.onEnterScope(component);
        }
    }

    @Override
    public void onExitScope(Object component) {
        for(ScopeWatcher w : watchers) {
            w.onExitScope(component);
        }
    }
}