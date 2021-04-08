package github.hotstu.maotai.module;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import github.hotstu.labo.jsbridge.annotation.JavaInterface;
import github.hotstu.labo.jsbridge.annotation.Param;
import github.hotstu.labo.jsbridge.annotation.ParamCallback;
import github.hotstu.labo.jsbridge.interact.ResponseCode;
import github.hotstu.labo.jsbridge.interfaces.IJavaReplyToJsString;
import github.hotstu.maotai.HybridUI;
import github.hotstu.maotai.bean.AppInfo;
import github.hotstu.maotai.bean.NativeEvent;
import github.hotstu.maotai.bean.RectParam;
import github.hotstu.maotai.bean.WinParam;
import github.hotstu.maotai.engine.CoreFragment;
import github.hotstu.maotai.engine.EventFilter;
import github.hotstu.maotai.engine.JsBridgeView;
import github.hotstu.naiue.util.MODisplayHelper;
import github.hotstu.naiue.util.MOStatusBarHelper;
import github.hotstu.naiue.util.MOViewHelper;
import io.reactivex.functions.Function3;

/**
 * @author hglf
 * @since 2018/8/1
 */
public class CoreFrame implements LifecycleObserver {
    private static final String TAG = CoreFrame.class.getSimpleName();
    private final CoreFragment fragment;
    private final JsBridgeView currentJsBridgeView;
    private final EventFilter eventFilter;
    private final Handler handler;


    public CoreFrame(CoreFragment fragment, JsBridgeView view) {
        this.eventFilter = view.getEventFilter();
        this.fragment = fragment;
        this.currentJsBridgeView = view;
        handler = new Handler(Looper.getMainLooper());
        view.getLifecycle().addObserver(this);
    }

    @JavaInterface("closeFrame")
    public void closeFrame(@Param("name") String name) {
        JsBridgeView container = fragment.getContainer();
        if (name == null || "".equals(name)) {
            if (CoreFragment.ROOT_FRAME_TAG.equals(currentJsBridgeView.getFrameName())) {
                Log.w(TAG, "can't close current frame is root");
                return;
            } else {
                container.removeView(currentJsBridgeView);
                return;
            }
        }
        int count = container.getChildCount();
        JsBridgeView toRemove = null;
        for (int i = count - 1; i >= 0; i--) {
            View v = container.getChildAt(i);
            if (v instanceof JsBridgeView) {
                JsBridgeView temp = (JsBridgeView) v;
                if (temp.getFrameName() != null && temp.getFrameName().equals(name)) {
                    toRemove = temp;
                    break;
                }
            }
        }
        // destory removed frame
        if (toRemove != null) {
            container.removeView(toRemove);
            toRemove.destory();
        }
    }

    /**
     * @param winName
     * @param frameName
     * @return
     */
    @JavaInterface("getPageParam")
    public JSONObject getPageParam(@Param("winName") String winName, @Param("frameName") String frameName) {
        CoreFragment currentFragment;
        if (isEmpty(winName)) {
            currentFragment = fragment;
        } else {
            currentFragment = fragment.getUI().getCoreFragmentByTag(winName);
        }
        if (currentFragment == null) {
            return null;
        }
        //指定了目标窗口
        if (isEmpty(frameName)) {
            if (currentFragment == fragment) {
                return currentJsBridgeView.getPageParam();
            } else {
                return currentFragment.getContainer().getPageParam();
            }
        } else {
            JsBridgeView childByName = currentFragment.getContainer().getChildByName(frameName);
            if (childByName != null) {
                return childByName.getPageParam();
            } else {
                return null;
            }
        }
    }

    //region 窗口管理
    @JavaInterface("openWin")
    public void openWin(@Param("name") String name, @Param("url") String url, @Param("pageParam") JSONObject pageParam) {
        fragment.getUI().startFragment(CoreFragment.newInstance(new WinParam(name, url, pageParam)));
    }

    @JavaInterface("closeWin")
    public void closeWin(@Param("name") String name) {
        //TODO bug 当name为空时关闭当前页
        if (name == null || "".equals(name)) {
            name = fragment.getTagNmae();
        }
        fragment.getUI().popBackStackInclusive(name);
    }

    @JavaInterface("openFrame")
    public void openFrame(@Param("name") String name, @Param("url") String url, @Param("rect") RectParam rect, @Param("pageParam") JSONObject pageParam) {
        if (name == null || "".equals(name)) {
            Log.w(TAG, "openFrame with name=" + name + ", use generated name instead");
            name = "md_frame_" + MOViewHelper.generateViewId();
        }
        if (CoreFragment.ROOT_FRAME_TAG.equals(name)) {
            Log.w(TAG, "openFrame with name=" + name + ", use generated name instead");
            name = "md_frame_" + MOViewHelper.generateViewId();
        }

        JsBridgeView view = fragment.getContainer();

        // 如果同名frame已经存在,将目标frame移动到最上层
        JsBridgeView child = view.getChildByName(name);
        if (child != null) {
            child.bringToFront();
            return;
        }
        WinParam winParam = new WinParam(name, url, pageParam);
        JsBridgeView jsView = new JsBridgeView(fragment, winParam);
        jsView.setBackgroundColor(Color.YELLOW);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (rect != null) {
            layoutParams.height = rect.height >= 0 ? MODisplayHelper.dp2px(fragment.requireContext(), rect.height) : rect.height;
            layoutParams.width = rect.width >= 0 ? MODisplayHelper.dp2px(fragment.requireContext(), rect.width) : rect.width;
            layoutParams.topMargin = rect.top >= 0 ? MODisplayHelper.dp2px(fragment.requireContext(), rect.top) : rect.top;
            layoutParams.leftMargin = rect.left >= 0 ? MODisplayHelper.dp2px(fragment.requireContext(), rect.left) : rect.left;
        }
        jsView.setLayoutParams(layoutParams);
        view.addView(jsView);
    }

    //endregion

    //region ui
    @JavaInterface("toast")
    public void toast(@Param("msg") String msg) {
        Toast.makeText(fragment.requireContext(), msg, Toast.LENGTH_LONG).show();
    }

    @JavaInterface("statusBarHeight")
    public int getStatusBarHeight() {
        return MODisplayHelper.px2dp(fragment.requireContext(), MOStatusBarHelper.getStatusbarHeight(fragment.requireContext()));
    }

    @JavaInterface("getAppInfo")
    public AppInfo getAppInfo() {
        AppInfo info = new AppInfo();
        info.appVersion = fragment.getUI().appVersion();
        info.statusBarHeight = getStatusBarHeight();
        info.pageParam = getPageParam(null, null);
        return info;
    }
    //endregion

    @JavaInterface("testAsync")
    public void testCB(@ParamCallback IJavaReplyToJsString cb) {
        final IJavaReplyToJsString ref = cb;
        handler.postDelayed(() -> ref.replyToJs(ResponseCode.RES_SUCCESS, null, "hello,world"), 100);
    }

    //region events
    @JavaInterface("addEventListener")
    public void addEventListener(@Param("name") String eventName) {
        eventFilter.regester(eventName);
    }

    @JavaInterface("removeEventListener")
    public void removeEventListener(@Param("name") String eventName) {
        eventFilter.unRegester(eventName);
    }

    @JavaInterface("isListening")
    public boolean isListening(@Param("name") String eventName) {
        return eventFilter.isRegestered(eventName);
    }

    @JavaInterface("sendEvent")
    public void sendEvent(
            @Param("name") String eventName,
            @Param("winName") String winName,
            @Param("frameName") String frameName,
            @Param("extra") JSONObject extra) {
        NativeEvent<JSONObject> customEvent = NativeEvent.createCustomEvent(eventName, extra);
        Function3<HybridUI, CoreFragment, JsBridgeView, Void> func = (ui, fragment, view) -> {
            view.getJsInterface().sendNativeEventToJS(customEvent);
            return null;
        };
        if (isEmpty(winName) && isEmpty(frameName)) {
            //send to all
            runInAllScope(func);
            return;
        }
        if (isEmpty(winName)) {
            winName = fragment.getTagNmae();
        }
        //指定了目标窗口
        if (isEmpty(frameName)) {
            //send to  window scope
            runInWindowScope(winName, func);
        } else {
            //send to frame scope
            runInFrameScope(winName, frameName, func);
        }
    }

    /**
     * 如果只指定了frameName，在当前window执行，
     * 如果只指定了 winName，在当前window的root frame执行，
     * 如果都没有指定，不执行
     *
     * @param winName
     * @param frameName
     * @param script
     */
    @JavaInterface("execScript")
    public void execScript(
            @Param("winName") String winName,
            @Param("frameName") String frameName,
            @Param("script") String script) {
        if (isEmpty(winName) && isEmpty(frameName)) {
            Log.e(TAG, "execScript error winName and frameName both null");
            return;
        }
        CoreFragment currentFragment;
        if (isEmpty(winName)) {
            currentFragment = fragment;
        } else {
            currentFragment = fragment.getUI().getCoreFragmentByTag(winName);
        }
        if (currentFragment == null) {
            return;
        }
        //指定了目标窗口
        if (isEmpty(frameName)) {
            if (currentFragment.getContainer() != null) {
                currentFragment.getContainer().execRawJS(script);
            }
        } else {
            JsBridgeView childByName = currentFragment.getContainer().getChildByName(frameName);
            if (childByName != null) {
                childByName.execRawJS(script);
            }
        }
    }
    //regionend

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onViewDestoy() {
        Log.d(TAG, "JsBridgeView event" + currentJsBridgeView.getLifecycle().getCurrentState());
        eventFilter.clear();
    }

    //region util method
    private Void runInAllScope(Function3<HybridUI, CoreFragment, JsBridgeView, Void> func) {
        HybridUI activity = fragment.getUI();
        List<CoreFragment> allCoreFragment = activity.getAllCoreFragment();
        try {
            for (int i = 0; i < allCoreFragment.size(); i++) {
                CoreFragment targetFragment = allCoreFragment.get(i);
                func.apply(activity, targetFragment, targetFragment.getContainer());
                List<JsBridgeView> allJBViewChildren = targetFragment.getContainer().getAllJBViewChildren();
                for (int j = 0; j < allJBViewChildren.size(); j++) {
                    func.apply(activity, targetFragment, allJBViewChildren.get(j));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected Void runInWindowScope(String winName, Function3<HybridUI, CoreFragment, JsBridgeView, Void> func) {
        HybridUI activity = fragment.getUI();
        CoreFragment targetFragment = activity.getCoreFragmentByTag(winName);
        if (targetFragment == null) {
            return null;
        }
        try {
            func.apply(activity, targetFragment, targetFragment.getContainer());
            List<JsBridgeView> allJBViewChildren = targetFragment.getContainer().getAllJBViewChildren();
            for (int i = 0; i < allJBViewChildren.size(); i++) {
                func.apply(activity, targetFragment, allJBViewChildren.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Void runInFrameScope(String winName, String frameName, Function3<HybridUI, CoreFragment, JsBridgeView, Void> func) {
        HybridUI activity = fragment.getUI();
        CoreFragment targetFragment = activity.getCoreFragmentByTag(winName);
        if (targetFragment == null) {
            return null;
        }
        JsBridgeView targetView = targetFragment.getContainer().getChildByName(frameName);
        if (targetView == null) {
            return null;
        }
        try {
            func.apply(activity, targetFragment, targetView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isEmpty(String input) {
        return input == null || "".equals(input);
    }
    //endregion

}
