package io.github.hotstu.maotai.provider;

import android.view.KeyEvent;

/**
 * DF遍历各个节点依次调用onKeyPressed，直到返回true，
 *如果没有处理并且是返回键，再DF遍历各节点onBackPressed，直到返回true
 * @author hglf
 * @since 2018/7/27
 */
public interface IKeyPressAware {
    boolean onBackPressed();
    default boolean onKeyPressed(int keyCode, KeyEvent event){
        return false;
    }
}
