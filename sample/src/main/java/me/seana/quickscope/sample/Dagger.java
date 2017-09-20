package me.seana.quickscope.sample;

import android.app.Application;
import android.content.Context;

import me.seana.quickscope.QuickScope;
import me.seana.quickscope.ScopeTree;

public final class Dagger {

    private static ScopeTree tree;

    public static ScopeTree getTree(Context context) {
        if (tree == null) {
            synchronized (Dagger.class) {
                if (tree == null) {
                    Application application = (Application) context.getApplicationContext();
                    ApplicationComponent component = DaggerApplicationComponent.builder()
                            .applicationModule(new ApplicationModule(application))
                            .build();
                    QuickScope root = new QuickScope.Builder(component)
                            .key(application)
                            .build();
                    tree = new ScopeTree.Builder()
                            .root(root)
                            .addWatcher(new LoggingScopeWatcher())
                            .build();
                }
            }
        }
        return tree;
    }

    private Dagger() {
        throw new AssertionError("No instances.");
    }

}