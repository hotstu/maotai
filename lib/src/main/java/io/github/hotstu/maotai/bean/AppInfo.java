package io.github.hotstu.maotai.bean;

import org.json.JSONObject;

import io.github.hotstu.jsbridge.annotation.Param;


/**
 * @author hglf
 * @since 2018/8/10
 */
public class AppInfo {
    @Param("appVersion") public String appVersion ="";
    @Param("statusBarHeight") public int statusBarHeight = 0;
    @Param("pageParam") public JSONObject pageParam = null;
}
