package github.hotstu.maotai.engine;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import github.hotstu.labo.jsbridge.core.NIMJsBridgeBuilder;
import github.hotstu.maotai.module.Core;
import github.hotstu.maotai.module.CoreFrame;

/**
 * @author hglf
 * @since 2018/8/1
 */
public class MDJsBridgeBuilder extends NIMJsBridgeBuilder<MDJsBridgeBuilder> {
    /**
     * 简单的依赖注入生成JavaInterface
     */
    public interface JavaInterfaceFactory {
        Object create(Activity activity, CoreFragment fragment, JsBridgeView view);
    }

    /**
     * Fragment Scope Java Interface Factory
     */
    private static class CoreInterfaceFactory implements JavaInterfaceFactory {
        @Override
        public Object create(Activity activity, CoreFragment fragment, JsBridgeView view) {
            return  new Core(fragment);
        }
    }

    /**
     * webView scope Java Interface Facotry
     */
    private static class FrameInterfaceFactory implements JavaInterfaceFactory {

        @Override
        public Object create(Activity activity, CoreFragment fragment, JsBridgeView view) {
            return new CoreFrame(fragment, view);
        }
    }
    private final Activity activity;
    private final CoreFragment fragment;
    private final List<JavaInterfaceFactory> factoryList;
    private  JsBridgeView view;

    public MDJsBridgeBuilder(CoreFragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        factoryList = new ArrayList<>();
        addInterfaceFacotry(new CoreInterfaceFactory());
        addInterfaceFacotry(new FrameInterfaceFactory());
    }

    public MDJsBridgeBuilder setJsBridgeView(JsBridgeView view) {
        this.view = view;
        setWebView(view.getWebView());
        return this;
    }


    public MDJsBridgeBuilder addInterfaceFacotry(JavaInterfaceFactory factory) {
        if (factoryList.indexOf(factory) < 0) {
            factoryList.add(factory);
        }
        return this;
    }

    /**
     * warn: only used for NIMJsBridge creation
     * @return
     */
    @Override
    public ArrayList getJavaInterfacesForJS() {
        ArrayList mergedList = new ArrayList();
        mergedList.addAll(super.getJavaInterfacesForJS());
        for (int i = 0; i < factoryList.size(); i++) {
            mergedList.add(factoryList.get(i).create(activity, fragment, view));
        }
        return mergedList;
    }

}
