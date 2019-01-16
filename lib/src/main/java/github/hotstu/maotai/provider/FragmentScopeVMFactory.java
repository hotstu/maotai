package github.hotstu.maotai.provider;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author hglf
 * @since 2018/8/6
 */
public class FragmentScopeVMFactory extends ViewModelProvider.NewInstanceFactory {
    private final Application app;

    public FragmentScopeVMFactory(Application app) {
        this.app = app;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CoreFragmentViewModel.class)) {
            //noinspection unchecked
            return (T) new CoreFragmentViewModel(app);
        } else
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
