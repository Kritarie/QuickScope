package me.seana.quickscope.sample;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import me.seana.quickscope.ScopedModule;

@Module
public class ApplicationModule extends ScopedModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    public Application provideApplication() {
        return this.application;
    }

}