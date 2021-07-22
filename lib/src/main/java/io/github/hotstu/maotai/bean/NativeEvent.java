package io.github.hotstu.maotai.bean;

import android.view.KeyEvent;

import org.json.JSONObject;

import io.github.hotstu.jsbridge.annotation.Param;


/**
 * @author hglf
 * @since 2018/7/31
 */
public class NativeEvent<T> {
    public static final String BULDIN_EVENT_KEYPRESS = "keypress";
    public static final String BULDIN_EVENT_BACKPRESS = "backpress";
    @Param("name") public String name;
    @Param("extra") public T extra;

    public static NativeEvent<Integer> createKeyPressEvent(int keyCode, KeyEvent event) {
        NativeEvent<Integer> ev = new NativeEvent<>();
        ev.name = BULDIN_EVENT_KEYPRESS;
        ev.extra = keyCode;
        return ev;
    }

    public static NativeEvent<Integer> createBackPressEvent(int keyCode, KeyEvent event) {
        NativeEvent<Integer> ev = new NativeEvent<>();
        ev.name = BULDIN_EVENT_BACKPRESS;
        ev.extra = keyCode;
        return ev;
    }

    public static NativeEvent<JSONObject> createCustomEvent(String name, JSONObject extra) {
        NativeEvent<JSONObject> ev = new NativeEvent<>();
        ev.name = name;
        ev.extra = extra;
        return ev;
    }
}
