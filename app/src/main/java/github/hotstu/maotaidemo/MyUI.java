package github.hotstu.maotaidemo;

import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import java.util.List;

import io.github.hotstu.maotai.engine.CoreFragment;
import io.github.hotstu.maotai.engine.MDJsBridgeBuilder;
import io.github.hotstu.moui.arch.MOFragment;

/**
 * @author hglf
 * @since 2018/8/8
 */
public class MyUI extends UI {
    @Override
    public List<MDJsBridgeBuilder.JavaInterfaceFactory> getJavaInterfaceFactory() {
        //通过这个接口添加自定义的JavaInterface
        return null;
    }

    @Override
    public WebChromeClient getCustomWebChromeClient() {
        return null;
    }

    @Override
    public WebViewClient getCustomWebViewClient() {
        return null;
    }

    @Override
    public String getCustomUserAgentString() {
        return null;
    }

    @Override
    public String appVersion() {
        return null;
    }

    @Override
    public int getSourceType() {
        return 0;
    }

    @Override
    public void startFragment(CoreFragment fragment) {
        super.startFragment((MOFragment) fragment);
    }
}
