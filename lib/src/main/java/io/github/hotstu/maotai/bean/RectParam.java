package io.github.hotstu.maotai.bean;

import android.widget.FrameLayout;

import io.github.hotstu.jsbridge.annotation.Param;


/**
 * @author hglf
 * @since 2018/7/27
 */
public class RectParam {
    @Param("top") public int top = 0;
    @Param("left") public int left = 0;
    @Param("width") public int width = FrameLayout.LayoutParams.MATCH_PARENT;
    @Param("height") public int height = FrameLayout.LayoutParams.MATCH_PARENT;
}
