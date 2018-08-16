package github.hotstu.maotai.provider;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import github.hotstu.maotai.UI;

/**
 * @author hglf
 * @since 2018/8/6
 */
public class ActivityScopeVMFactory extends ViewModelProvider.NewInstanceFactory {
    private final UI activity;

    public ActivityScopeVMFactory(UI activity) {
        this.activity = activity;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UIViewModel.class)) {
            //noinspection unchecked
            return (T) new UIViewModel(activity.getApplication(), activity.getCustomUserAgentString(), activity.getDefaultSourceType());
        } else
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
