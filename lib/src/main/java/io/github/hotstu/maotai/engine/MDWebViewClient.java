package io.github.hotstu.maotai.engine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.github.hotstu.jsbridge.core.WebViewClientDelegate;

/**
 * @author hglf
 * @since 2018/8/3
 */
public class MDWebViewClient extends WebViewClientDelegate {

    private final MDRouter router;

    public MDWebViewClient(Activity activity, WebViewClient delegate) {
        super(delegate);
        router = new MDRouter(activity);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return router.to(request.getUrl().toString()) || super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return router.to(url) || super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return super.shouldInterceptRequest(view, url);
    }
}
