package github.hotstu.maotai.module;

import github.hotstu.labo.jsbridge.annotation.JSInterface;
import github.hotstu.labo.jsbridge.annotation.Param;
import github.hotstu.labo.jsbridge.interfaces.InvocationFuture;
import github.hotstu.maotai.bean.NativeEvent;

/**
 * @author hglf
 * @since 2018/7/31
 */
public interface CoreJSInterface {
    @JSInterface("onNativeEvent")
    InvocationFuture sendNativeEventToJS(@Param("event") NativeEvent event);
}