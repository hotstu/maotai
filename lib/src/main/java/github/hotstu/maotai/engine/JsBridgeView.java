package github.hotstu.maotai.engine;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import github.hotstu.labo.jsbridge.core.NIMJsBridge;
import github.hotstu.labo.jsbridge.util.JsUtil;
import github.hotstu.labo.jsbridge.util.WebViewConfig;
import github.hotstu.maotai.R;
import github.hotstu.maotai.UI;
import github.hotstu.maotai.bean.NativeEvent;
import github.hotstu.maotai.bean.WinParam;
import github.hotstu.maotai.module.CoreJSInterface;
import github.hotstu.maotai.provider.CoreFragmentViewModel;
import github.hotstu.maotai.provider.IKeyPressAware;
import github.hotstu.maotai.provider.Injection;
import github.hotstu.maotai.provider.UIViewModel;
import github.hotstu.maotai.widget.MDInsetFrameLayout;

import static github.hotstu.maotai.bean.NativeEvent.BULDIN_EVENT_BACKPRESS;
import static github.hotstu.maotai.bean.NativeEvent.BULDIN_EVENT_KEYPRESS;


/**
 * @author hglf
 * @since 2018/7/23
 */
public class JsBridgeView extends MDInsetFrameLayout implements IKeyPressAware, LifecycleOwner {
    private static final String TAG = JsBridgeView.class.getSimpleName();
    private final WinParam winParam;
    private final LifecycleRegistry mLifecycleRegistry;
    private final UI ui;
    private WebView webView;
    private NIMJsBridge jsBridge;
    private CoreJSInterface jsInterface;
    private final EventFilter eventFilter;


    public JsBridgeView(@NonNull CoreFragment fragment, @NonNull WinParam winParam) {
        super(fragment.getUI());
        ui = fragment.getUI();
        mLifecycleRegistry = new LifecycleRegistry(this);
        this.setFrameName(winParam.name);
        this.eventFilter = new EventFilter();
        this.winParam = winParam;
        //TODO 增加loading indicator
        LayoutInflater.from(getContext()).inflate(R.layout.md_view_js_bridge, this, true);
        setFitsSystemWindows(true);
        webView = findViewById(R.id.webView);
        webView.setFitsSystemWindows(true);

        initWebView();
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        UIViewModel vm = ViewModelProviders.of(ui, Injection.getViewModelFactory(ui)).get(UIViewModel.class);
        CoreFragmentViewModel fvm = ViewModelProviders.of( fragment, Injection.getViewModelFactory(fragment))
                .get(CoreFragmentViewModel.class);
        jsBridge = Injection.getJSBridgeBuilder(fragment).setJsBridgeView(this).create();
        vm.getMdConfigLiveData().observe(this,  mdConfig -> {
            assert mdConfig != null;
            setBackgroundColor(mdConfig.getParsedColor());
            if (mdConfig.userAgent != null) {
                webView.getSettings().setUserAgentString(mdConfig.userAgent);
            }
            webView.loadUrl(mdConfig.getRealPath(winParam.url));
            //webView.setFitsSystemWindows(mdConfig.translucentStatusbar);
        });
    }


    private void initWebView( ) {
        WebSettings settings = webView.getSettings();
        WebViewConfig.setWebSettings(getContext(), settings, getContext().getApplicationInfo().dataDir);
        //允许跨域访问
        settings.setAllowUniversalAccessFromFileURLs(true);
        WebViewConfig.removeJavascriptInterfaces(webView);
        WebViewConfig.setWebViewAllowDebug(false);
        WebViewConfig.setAcceptThirdPartyCookies(webView);
        //settings.setUserAgentString();
        webView.setOnLongClickListener(null);
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            //TODO handle download
            Log.d(TAG, "onDownloadStart");
        });
    }

    public void setFrameName(String name) {
        setTag(R.id.md_frame_name, name);
    }

    public String getFrameName() {
        String ret = (String) getTag(R.id.md_frame_name);
        return ret;
    }

    public WebView getWebView() {
        return webView;
    }

    public EventFilter getEventFilter() {
        return eventFilter;
    }

    public CoreJSInterface getJsInterface() {
        if (jsInterface == null) {
            jsInterface = jsBridge.getJsInterface(CoreJSInterface.class);
        }
        return jsInterface;
    }

    public NIMJsBridge getJsBridge() {
        return jsBridge;
    }

    public void execRawJS(String script) {
        JsUtil.callJS(webView, script);
    }

    public JSONObject getPageParam() {
        return winParam.pageParam;
    }
    public @Nullable
    JsBridgeView getChildByName(String name) {
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View v = getChildAt(i);
            if (v instanceof JsBridgeView && name.equals(((JsBridgeView) v).getFrameName())) {
                return ((JsBridgeView) v);
            }
        }
        return null;
    }

    public List<JsBridgeView> getAllJBViewChildren() {
        int childCount = getChildCount();
        List<JsBridgeView> list = new ArrayList<>();
        for (int i = childCount - 1; i >= 0; i--) {
            View v = getChildAt(i);
            if (v instanceof JsBridgeView) {
                list.add((JsBridgeView) v);
            }
        }
        return list;
    }

    public void destory() {
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        List<JsBridgeView> allJBViewChildren = getAllJBViewChildren();
        for (int i = 0; i < allJBViewChildren.size(); i++) {
            allJBViewChildren.get(i).destory();
        }
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public boolean onKeyPressed(int keyCode, KeyEvent event) {
        int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View v = getChildAt(i);
            if (v instanceof IKeyPressAware && ((IKeyPressAware) v).onKeyPressed(keyCode, event)) {
                return true;
            }
        }
        //backpress 事件优先级高于keypress
        if (keyCode == KeyEvent.KEYCODE_BACK && this.eventFilter.isRegestered(BULDIN_EVENT_BACKPRESS)) {
            getJsInterface().sendNativeEventToJS(NativeEvent.createBackPressEvent(keyCode, event));
            return true;
        }
        if (this.eventFilter.isRegestered(BULDIN_EVENT_KEYPRESS)) {
            getJsInterface().sendNativeEventToJS(NativeEvent.createKeyPressEvent(keyCode, event));
            return true;
        }
        return false;
    }

    @Override
    public boolean onBackPressed() {
        int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View v = getChildAt(i);
            if (v instanceof IKeyPressAware && ((IKeyPressAware) v).onBackPressed()) {
                return true;
            }
        }
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @Override
    protected void onDetachedFromWindow() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onDetachedFromWindow();
    }

    /**
     * Returns the Lifecycle of the provider.
     *
     * @return The lifecycle of the provider.
     */
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }


}
