package github.hotstu.maotai.module;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.util.List;

import github.hotstu.labo.jsbridge.annotation.JavaInterface;
import github.hotstu.labo.jsbridge.annotation.Param;
import github.hotstu.maotai.UI;
import github.hotstu.maotai.bean.NativeEvent;
import github.hotstu.maotai.engine.CoreFragment;
import github.hotstu.maotai.engine.EventFilter;
import github.hotstu.maotai.engine.JsBridgeView;
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


    public CoreFrame(CoreFragment fragment, JsBridgeView view) {
        this.eventFilter = view.getEventFilter();
        this.fragment = fragment;
        this.currentJsBridgeView = view;
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
        if (isEmpty(winName) && isEmpty(frameName)) {
            Log.e(TAG, "pageParam error winName and frameName both null");
            return null;
        }
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
            return currentFragment.getContainer().getPageParam();
        } else {
            JsBridgeView childByName = currentFragment.getContainer().getChildByName(frameName);
            if (childByName != null) {
                return childByName.getPageParam();
            }
        }
        return null;
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
        Function3<UI, CoreFragment, JsBridgeView, Void> func = (ui, fragment, view) -> {
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
            currentFragment.getContainer().execRawJS(script);
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
    }

    //region util method
    private Void runInAllScope(Function3<UI, CoreFragment, JsBridgeView, Void> func) {
        UI activity = fragment.getUI();
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


    protected Void runInWindowScope(String winName, Function3<UI, CoreFragment, JsBridgeView, Void> func) {
        UI activity = fragment.getUI();
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

    protected Void runInFrameScope(String winName, String frameName, Function3<UI, CoreFragment, JsBridgeView, Void> func) {
        UI activity = fragment.getUI();
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
