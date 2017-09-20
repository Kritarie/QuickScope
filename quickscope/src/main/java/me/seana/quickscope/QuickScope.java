package me.seana.quickscope;

import java.util.LinkedHashMap;

import static me.seana.quickscope.Preconditions.checkNotNull;

public final class QuickScope {

    private final String key;
    private final QuickScope parent;
    private final Object component;
    private final LinkedHashMap<String, QuickScope> children;
    private ScopeWatcher watcher;
    private boolean destroyed;

    private QuickScope(Builder builder) {
        this.key = checkNotNull(builder.key, "key");
        this.parent = builder.parent;
        this.component = checkNotNull(builder.component, "component");
        this.children = new LinkedHashMap<>(1);
    }

    public static final class Builder {

        private final Object component;
        private String key;
        private QuickScope parent;

        public Builder(Object component) {
            this.component = component;
        }

        public Builder key(Object key) {
            this.key = key.toString();
            return this;
        }

        public Builder parent(QuickScope parent) {
            this.parent = parent;
            return this;
        }

        public QuickScope build() {
            return new QuickScope(this);
        }
    }

    @SuppressWarnings("unchecked")
    public QuickScope findScope(Object key) {
        checkNotDestroyed();
        if (this.key.equals(key.toString())) {
            return this;
        }
        QuickScope find = children.get(key.toString());
        if (find != null) {
            return find;
        }

        for (QuickScope child : children.values()) {
            find = child.findScope(key);
            if (find != null) {
                return find;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent() {
        return (T) this.component;
    }

    boolean isDestroyed() {
        return destroyed;
    }

    public boolean extend(QuickScope scope) {
        checkNotDestroyed();
        if (scope.parent == this) {
            attach(scope);
            return true;
        } else {
            for (QuickScope child : children.values()) {
                if (child.extend(scope))
                    return true;
            }
        }
        // could not extend scope (parent does not exist in tree)
        return false;
    }

    void setScopeWatcher(ScopeWatcher watcher) {
        this.watcher = watcher;
        for (QuickScope child : children.values()) {
            child.setScopeWatcher(watcher);
        }
    }

    public void destroy() {
        if (destroyed)
            return;
        destroyed = true;
        for (QuickScope child : children.values()) {
            child.destroy();
        }
        if (parent != null) {
            parent.children.remove(key);
        }
        dispatchExit(component);
    }

    private void attach(QuickScope scope) {
        scope.setScopeWatcher(watcher);
        children.put(scope.key, scope);
        dispatchEnter(scope.getComponent());
    }

    private void dispatchEnter(Object component) {
        if (component instanceof ScopedComponent) {
            for (Scoped s : ((ScopedComponent)component).getScopedObjects()) {
                s.onEnterScope();
            }
        }
        if (watcher != null) {
            watcher.onEnterScope(component);
        }
    }

    private void dispatchExit(Object component) {
        if (component instanceof ScopedComponent) {
            for (Scoped s : ((ScopedComponent)component).getScopedObjects()) {
                s.onExitScope();
            }
        }
        if (watcher != null) {
            watcher.onExitScope(component);
        }
    }

    private void checkNotDestroyed() {
        if (destroyed) {
            throw new IllegalStateException("Scope is destroyed.");
        }
    }

}