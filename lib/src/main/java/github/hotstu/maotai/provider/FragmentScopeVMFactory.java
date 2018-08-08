package github.hotstu.maotai.provider;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import github.hotstu.maotai.UI;
import github.hotstu.maotai.engine.CoreFragment;

/**
 * @author hglf
 * @since 2018/8/6
 */
public class FragmentScopeVMFactory extends ViewModelProvider.NewInstanceFactory {
    private final UI activity;
    private final CoreFragment fragment;

    public FragmentScopeVMFactory(UI activity, CoreFragment fragment) {
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CoreFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new CoreFragmentViewModel(activity.getApplication(), activity, fragment);
        } else
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
