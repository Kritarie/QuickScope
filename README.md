QuickScope
=====

QuickScope is a simple library for managing the lifetime of scoped objects on Java and Android.

How do I do?
============

Start by creating a root scope and initializing a ScopeTree. Monitor the correctness of your application by building the ScopeTree with a custom ScopeWatcher.

```java
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
```

Build on the tree to enter new, more specialized Scopes. Each QuickScope node should be uniquely identifiable by an Object key passed into the builder. This key is used to retrieve and later destroy the scope associated with it.

```java
// obtain singleton tree
ScopeTree tree = Dagger.getTree(this);
// identify parent scope in tree
QuickScope parent = tree.findScope(getApplicationContext());
// create dagger subcomponent
ActivityComponent component = parent.<ApplicationComponent>getComponent()
    .plus(new ActivityModule(this));
// create new child scope
QuickScope scope = new QuickScope.Builder(component) // with subcomponent
    .key(this) // keyed to this activity (subcomponent is activity scoped)
    .parent(parent) // specify the parent scope
    .build();
tree.extend(scope);
```

At each scope depth, define any number of scope-aware objects with the ScopedComponent.

```java
public class MyComponent implements ScopedComponent {

    private final Set<Scoped> scoped;

    public MyComponent(Set<Scoped> scoped) {
        this.scoped = scoped;
    }

    @Override
    public Set<Scoped> getScopedObjects() {
        return this.scoped;
    }

}
```

```java
QuickScope scope = new QuickScope.Builder(new MyComponent(someScopedObjects))
    .key(...)
    .root(...)
    .build();
```

An object in your component implementing the Scoped interface will be able to perform setup and teardown logic by receiving entry/exit callbacks from the ScopeTree.
```java
public class MyScopedObject implements Scoped {

    @Override
    public void onEnterScope() {
        // start listening to some system event
    }

    @Override
    public void onExitScope() {
        // stop listening
    }
}
```