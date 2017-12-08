package me.seana.quickscope;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class QuickScopeTest {

    @Mock
    ScopeWatcher watcher;
    @Mock
    Scoped scoped;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void watcher_should_enter_on_extend() {
        QuickScope root = new QuickScope.Builder(new TestComponent())
                .key("root")
                .build();
        ScopeTree tree = new ScopeTree.Builder()
                .root(root)
                .addWatcher(watcher)
                .build();
        ScopedComponent component = new TestComponent();
        QuickScope scope = new QuickScope.Builder(component)
                .key("child")
                .parent(root)
                .build();

        tree.extend(scope);

        verify(watcher).onEnterScope(component);
    }

    @Test
    public void watcher_should_exit_on_destroy() {
        QuickScope root = new QuickScope.Builder(new TestComponent())
                .key("root")
                .build();
        ScopeTree tree = new ScopeTree.Builder()
                .root(root)
                .addWatcher(watcher)
                .build();
        ScopedComponent component = new TestComponent();
        QuickScope scope = new QuickScope.Builder(component)
                .key("child")
                .parent(root)
                .build();
        tree.extend(scope);

        scope.destroy();

        verify(watcher).onExitScope(component);
    }

    @Test
    public void scoped_objects_should_enter_on_extend() {
        QuickScope root = new QuickScope.Builder(new TestComponent())
                .key("root")
                .build();
        ScopeTree tree = new ScopeTree.Builder()
                .root(root)
                .build();
        ScopedComponent component = new TestComponent(scoped);
        QuickScope scope = new QuickScope.Builder(component)
                .key("child")
                .parent(root)
                .build();

        tree.extend(scope);

        verify(scoped).onEnterScope();
    }

    @Test
    public void scoped_objects_should_exit_on_destroy() {
        QuickScope root = new QuickScope.Builder(new TestComponent())
                .key("root")
                .build();
        ScopeTree tree = new ScopeTree.Builder()
                .root(root)
                .build();
        ScopedComponent component = new TestComponent(scoped);
        QuickScope scope = new QuickScope.Builder(component)
                .key("child")
                .parent(root)
                .build();
        tree.extend(scope);

        scope.destroy();

        verify(scoped).onExitScope();
    }

    @Test
    public void destroy_parent_should_destroy_child() {
        QuickScope root = new QuickScope.Builder(new TestComponent())
                .key("root")
                .build();
        ScopeTree tree = new ScopeTree.Builder()
                .root(root)
                .build();
        ScopedComponent component = new TestComponent();
        QuickScope scope = new QuickScope.Builder(component)
                .key("child")
                .parent(root)
                .build();
        tree.extend(scope);

        root.destroy();

        assertThat(scope.isDestroyed()).isTrue();
    }

    @Test
    public void find_root_should_return_root() {
        TestComponent component = new TestComponent();
        QuickScope root = new QuickScope.Builder(component)
                .key("root")
                .build();
        ScopeTree tree = new ScopeTree.Builder()
                .root(root)
                .build();

        QuickScope find = tree.findScope("root");

        assertThat(find).isNotNull();
        assertThat(find).isEqualTo(root);
    }

    @Test
    public void find_child_should_return_child() {
        QuickScope root = new QuickScope.Builder(new TestComponent())
                .key("root")
                .build();
        ScopeTree tree = new ScopeTree.Builder()
                .root(root)
                .build();
        TestComponent component = new TestComponent();
        QuickScope scope = new QuickScope.Builder(component)
                .key("child")
                .parent(root)
                .build();

        tree.extend(scope);
        QuickScope find = tree.findScope("child");

        assertThat(find).isNotNull();
        assertThat(find).isEqualTo(scope);
    }

    @Test
    public void extend_should_insert_child() {
        QuickScope root = new QuickScope.Builder(new TestComponent())
                .key("root")
                .build();
        ScopeTree tree = new ScopeTree.Builder()
                .root(root)
                .build();
        TestComponent component = new TestComponent();
        QuickScope scope = new QuickScope.Builder(component)
                .key("child")
                .parent(root)
                .build();

        boolean success = tree.extend(scope);

        assertThat(success).isTrue();
    }
}