package github.hotstu.maotaidemo;

import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import java.util.List;

import github.hotstu.maotai.UI;
import github.hotstu.maotai.engine.MDJsBridgeBuilder;

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
}
