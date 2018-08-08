package github.hotstu.maotai.provider;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import github.hotstu.maotai.UI;
import github.hotstu.maotai.engine.CoreFragment;
import github.hotstu.maotai.engine.MDJsBridgeBuilder;
import github.hotstu.maotai.engine.MDWebChromeClient;
import github.hotstu.maotai.engine.MDWebViewClient;

/**
 * @author hglf
 * @since 2018/8/6
 */
public class CoreFragmentViewModel extends AndroidViewModel {

    private MDJsBridgeBuilder mBuilder;
    private CoreFragment fragment;

    public CoreFragmentViewModel(@NonNull Application application, UI activity, CoreFragment fragment) {
        super(application);
        this.fragment = fragment;
    }

    public MDJsBridgeBuilder getJSBridgeBuilder( ) {
        if (mBuilder == null) {
            mBuilder = new MDJsBridgeBuilder(fragment)
                    .setWebChromeClient(new MDWebChromeClient(null))
                    .setWebViewClient(new MDWebViewClient(fragment.getUI(), null));
            List<MDJsBridgeBuilder.JavaInterfaceFactory> factories = fragment.getUI().getJavaInterfaceFactory();
            if (factories != null) {
                for (MDJsBridgeBuilder.JavaInterfaceFactory factory : factories) {
                    mBuilder.addInterfaceFacotry(factory);
                }
            }
        }
        return mBuilder;
    }
}
