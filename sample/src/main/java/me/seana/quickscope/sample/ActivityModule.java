package me.seana.quickscope.sample;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import me.seana.quickscope.ScopedModule;

@Module
public class ActivityModule extends ScopedModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    public Activity provideActivity() {
        return this.activity;
    }

}