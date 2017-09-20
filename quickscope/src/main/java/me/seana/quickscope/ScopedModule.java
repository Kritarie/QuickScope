package me.seana.quickscope;

import java.util.Collections;
import java.util.Set;

import dagger.Provides;

public class ScopedModule {

    @Provides
    public Set<Scoped> provideScopedObjects() {
        return Collections.emptySet();
    }

}