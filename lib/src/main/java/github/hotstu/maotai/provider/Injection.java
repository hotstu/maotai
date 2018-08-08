package github.hotstu.maotai.provider;

import github.hotstu.maotai.UI;
import github.hotstu.maotai.engine.CoreFragment;

/**
 * @author hglf
 * @since 2018/1/16
 */
public class Injection {

    //activity scope
    public static ActivityScopeVMFactory getViewModelFactory(UI activty) {
        return new ActivityScopeVMFactory(activty);
    }

    //fragment scope
    public static FragmentScopeVMFactory getViewModelFactory(UI activity, CoreFragment fragment) {
        return new FragmentScopeVMFactory(activity, fragment);
    }

}
