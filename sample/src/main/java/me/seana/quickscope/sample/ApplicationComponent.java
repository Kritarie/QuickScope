package me.seana.quickscope.sample;

import dagger.Component;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    ActivityComponent plus(ActivityModule module);
}