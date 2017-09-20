package me.seana.quickscope.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.seana.quickscope.QuickScope;
import me.seana.quickscope.ScopeTree;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Context context = getContext();
        ScopeTree tree = Dagger.getTree(context);
        QuickScope parent = tree.findScope(context);
        FragmentComponent component = parent.<ActivityComponent>getComponent()
                .plus(new FragmentModule(this));
        QuickScope scope = new QuickScope.Builder(component)
                .key(this)
                .parent(parent)
                .build();
        tree.extend(scope);
    }

    @Override
    public void onDestroyView() {
        Context context = getContext();
        ScopeTree tree = Dagger.getTree(context);
        tree.destroy(this);
        super.onDestroyView();
    }
}