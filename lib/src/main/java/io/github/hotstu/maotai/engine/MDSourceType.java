package io.github.hotstu.maotai.engine;

import android.content.Context;

/**
 * @author hglf
 * @since 2018/8/6
 */
public enum MDSourceType {

    ASSETS(0),
    FOLDER(1);

    private static final String ASSET_PATH = "/android_asset/widget";
    private final int value;

    MDSourceType(int i) {
        this.value = i;
    }

    public String getSourcePath(Context context) {
        switch (value) {
            case 1:
                return context.getExternalFilesDir("widget").getAbsolutePath();
            default:
                return ASSET_PATH;
        }
    }

    public static MDSourceType fromValue(int value) {
        switch (value) {
            case 1:
                return MDSourceType.FOLDER;
            default:
                return MDSourceType.ASSETS;
        }
    }
}
