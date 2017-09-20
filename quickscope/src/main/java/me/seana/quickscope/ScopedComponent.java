package me.seana.quickscope;

import java.util.Set;

public interface ScopedComponent {
    Set<Scoped> getScopedObjects();
}