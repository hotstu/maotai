package io.github.hotstu.maotai.module;

import io.github.hotstu.maotai.bean.NativeEvent;
import io.github.hotstu.jsbridge.annotation.JSInterface;
import io.github.hotstu.jsbridge.annotation.Param;
import io.github.hotstu.jsbridge.interfaces.InvocationFuture;

/**
 * @author hglf
 * @since 2018/7/31
 */
public interface CoreJSInterface {
    @JSInterface("onNativeEvent")
    InvocationFuture sendNativeEventToJS(@Param("event") NativeEvent event);
}