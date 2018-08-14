package github.hotstu.maotai.module;

import android.arch.lifecycle.LifecycleObserver;

import github.hotstu.maotai.engine.CoreFragment;


/**
 * @author hglf
 * @since 2018/7/20
 */
@Deprecated
public class Core implements LifecycleObserver {
    private static final String TAG = Core.class.getSimpleName();
    public Core(CoreFragment fragment) {
    }
}
