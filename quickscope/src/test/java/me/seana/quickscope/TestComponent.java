package me.seana.quickscope;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@TestScope
class TestComponent implements ScopedComponent {

    private final Set<Scoped> set;

    public TestComponent() {
        this(new HashSet<Scoped>());
    }

    public TestComponent(Scoped... set) {
        this(new HashSet<>(Arrays.asList(set)));
    }

    public TestComponent(Set<Scoped> set) {
        this.set = set;
    }

    @Override
    public Set<Scoped> getScopedObjects() {
        return set;
    }
}