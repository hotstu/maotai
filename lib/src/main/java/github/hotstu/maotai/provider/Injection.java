package github.hotstu.maotai.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import github.hotstu.maotai.HybridUI;
import github.hotstu.maotai.engine.CoreFragment;
import github.hotstu.maotai.engine.MDConfig;
import github.hotstu.maotai.engine.MDJsBridgeBuilder;
import github.hotstu.maotai.engine.MDWebChromeClient;
import github.hotstu.maotai.engine.MDWebViewClient;

/**
 * @author hglf
 * @since 2018/1/16
 */
public class Injection {

    private static Gson gson;

    public static MDConfig getMDConfig(CoreFragment fragment) {
        MDConfig config = new MDConfig(fragment.getUI().getApplication(),fragment.getUI().getSourceType());
        config.userAgent = fragment.getUI().getCustomUserAgentString();
        return config;
    }
    public static MDConfig getMDConfig(HybridUI ui) {
        MDConfig config = new MDConfig(ui.getApplication(),ui.getSourceType());
        config.userAgent = ui.getCustomUserAgentString();
        return config;
    }


    public static Gson getGson() {
        if (gson == null) {
            synchronized (Injection.class) {
                if (gson == null) {
                    gson = new GsonBuilder().create();
                }
            }
        }
        return gson;
    }

    public static MDJsBridgeBuilder getJSBridgeBuilder(  CoreFragment fragment) {
            MDJsBridgeBuilder mBuilder = new MDJsBridgeBuilder(fragment)
                    .setWebChromeClient(new MDWebChromeClient(fragment.getUI().getCustomWebChromeClient()))
                    .setWebViewClient(new MDWebViewClient(fragment.getActivity(), fragment.getUI().getCustomWebViewClient()));
            List<MDJsBridgeBuilder.JavaInterfaceFactory> factories = fragment.getUI().getJavaInterfaceFactory();
            if (factories != null) {
                for (MDJsBridgeBuilder.JavaInterfaceFactory factory : factories) {
                    mBuilder.addInterfaceFacotry(factory);
                }
            }

        return mBuilder;
    }

}
