package me.seana.quickscope;

import java.util.ArrayList;
import java.util.List;

import static me.seana.quickscope.Preconditions.checkNotNull;

public final class ScopeTree {

    private final QuickScope root;
    private ScopeWatcher watcher;

    private ScopeTree(Builder builder) {
        root = checkNotNull(builder.root, "root");
        if (builder.watchers.size() > 1) {
            watcher = new AggregateScopeWatcher(builder.watchers);
        } else if (!builder.watchers.isEmpty()) {
            watcher = builder.watchers.get(0);
        }
        root.setScopeWatcher(watcher);
    }

    public void setScopeWatcher(ScopeWatcher watcher) {
        this.watcher = checkNotNull(watcher, "watcher");
        this.root.setScopeWatcher(watcher);
    }

    public ScopeWatcher getScopeWatcher() {
        return this.watcher;
    }

    public static final class Builder {

        private QuickScope root;
        private List<ScopeWatcher> watchers = new ArrayList<>(2);

        public Builder root(QuickScope root) {
            this.root = root;
            return this;
        }

        public Builder addWatcher(ScopeWatcher watcher) {
            watchers.add(watcher);
            return this;
        }

        public ScopeTree build() {
            return new ScopeTree(this);
        }
    }

    public QuickScope findScope(Object key) {
        return root.findScope(checkNotNull(key, "key"));
    }

    public void destroy(Object key) {
        QuickScope scope = findScope(key);
        if (scope != null) {
            scope.destroy();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T findComponent(Object key) {
        QuickScope scope = findScope(key);
        return scope.getComponent();
    }

    public boolean extend(QuickScope quickScope) {
        return root.extend(quickScope);
    }

}