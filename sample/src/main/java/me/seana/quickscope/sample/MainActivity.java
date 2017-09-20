package me.seana.quickscope.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import me.seana.quickscope.QuickScope;
import me.seana.quickscope.ScopeTree;

public class MainActivity extends AppCompatActivity {

    private static final String FRAG_TAG = "FRAG_TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        if (fragment == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment(), FRAG_TAG)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        ScopeTree tree = Dagger.getTree(this);
        tree.destroy(this);
        super.onDestroy();
    }
}