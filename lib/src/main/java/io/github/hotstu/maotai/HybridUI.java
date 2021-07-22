package io.github.hotstu.maotai;

import android.app.Application;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import java.util.List;

import io.github.hotstu.maotai.engine.CoreFragment;
import io.github.hotstu.maotai.engine.MDJsBridgeBuilder;


public interface HybridUI {
    List<MDJsBridgeBuilder.JavaInterfaceFactory> getJavaInterfaceFactory();

    WebChromeClient getCustomWebChromeClient();

    WebViewClient getCustomWebViewClient();

    String getCustomUserAgentString();

    String appVersion();

    int getSourceType();

    Application getApplication();

    CoreFragment getCoreFragmentByTag(String tag);

    void startFragment(CoreFragment fragment);

    void popBackStackInclusive(String tag);

    List<CoreFragment> getAllCoreFragment();
}
