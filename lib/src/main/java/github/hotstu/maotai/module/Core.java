package github.hotstu.maotai.module;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONObject;

import github.hotstu.labo.jsbridge.annotation.JavaInterface;
import github.hotstu.labo.jsbridge.annotation.Param;
import github.hotstu.labo.jsbridge.annotation.ParamCallback;
import github.hotstu.labo.jsbridge.interact.ResponseCode;
import github.hotstu.labo.jsbridge.interfaces.IJavaReplyToJsString;
import github.hotstu.maotai.UI;
import github.hotstu.maotai.bean.RectParam;
import github.hotstu.maotai.bean.WinParam;
import github.hotstu.maotai.engine.CoreFragment;
import github.hotstu.maotai.engine.JsBridgeView;
import github.hotstu.naiue.util.MODisplayHelper;
import github.hotstu.naiue.util.MOStatusBarHelper;
import github.hotstu.naiue.util.MOViewHelper;


/**
 * @author hglf
 * @since 2018/7/20
 */
public class Core implements LifecycleObserver {
    private static final String TAG = Core.class.getSimpleName();
    private final UI mContext;
    private final Handler handler;
    private final CoreFragment fragment;

    public Core(CoreFragment fragment) {
        this.mContext = (UI) fragment.getActivity();
        this.fragment = fragment;
        handler = new Handler(Looper.getMainLooper());
        fragment.getLifecycle().addObserver(this);
    }

    //region 窗口管理
    @JavaInterface("openWin")
    public void openWin(@Param("name")String name, @Param("url")String url, @Param("pageParam")JSONObject pageParam) {
        mContext.startFragment(CoreFragment.newInstance(new WinParam(name,url, pageParam)));
    }

    @JavaInterface("closeWin")
    public void closeWin(@Param("name")String name) {
        mContext.popBackStackInclusive(name);
    }

    @JavaInterface("openFrame")
    public void openFrame(@Param("name")String name, @Param("url")String url, @Param("rect")RectParam rect, @Param("pageParam")JSONObject pageParam) {
        if (name == null || "".equals(name)) {
            Log.w(TAG, "openFrame with name=" + name+ ", use generated name instead");
            name = "md_frame_" + MOViewHelper.generateViewId();
        }
        if (CoreFragment.ROOT_FRAME_TAG.equals(name)) {
            Log.w(TAG, "openFrame with name=" + name+ ", use generated name instead");
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
            layoutParams.height = rect.height>=0?MODisplayHelper.dp2px(mContext,rect.height):rect.height;
            layoutParams.width = rect.width>=0?MODisplayHelper.dp2px(mContext,rect.width):rect.width;
            layoutParams.topMargin = rect.top>=0?MODisplayHelper.dp2px(mContext,rect.top):rect.top;
            layoutParams.leftMargin = rect.left>=0?MODisplayHelper.dp2px(mContext,rect.left):rect.left;
        }
        jsView.setLayoutParams(layoutParams);
        jsView.setFrameName(name);
        view.addView(jsView);
    }

    //endregion

    //region ui
    @JavaInterface("toast")
    public void toast(@Param("msg")String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    @JavaInterface("statusBarHeight")
    public int getStatusBarHeight() {
        if (!mContext.isTranslucent()) {
            return 0;
        }
        return  MODisplayHelper.px2dp(mContext,MOStatusBarHelper.getStatusbarHeight(mContext));
    }
    //endregion

    @JavaInterface("testAsync")
    public void testCB(@ParamCallback IJavaReplyToJsString cb) {
        final IJavaReplyToJsString ref = cb;
        handler.postDelayed(() -> ref.replyToJs(ResponseCode.RES_SUCCESS,null, "hello,world"), 100);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onFragmentDestoy() {
        Log.d(TAG, "onFragmentDestoy do clean");
        fragment.getContainer().destory();
    }
}
