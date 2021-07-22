package io.github.hotstu.maotai.engine;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

/**
 * @author hglf
 * @since 2018/8/3
 */
public class MDRouter {
    public MDRouter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public static final boolean DEFALT_POLICY = true;//对于错误的、不认识的默认拦截&DO Nothing
    /**
     * http scheme
     */
    public static final String HTTP_SCHEME = "http";
    /**
     * https scheme
     */
    public static final String HTTPS_SCHEME = "https";
    /**
     * intent ' s scheme
     */
    public static final String INTENT_SCHEME = "intent";
    /**
     * SMS scheme
     */
    public static final String SCHEME_SMS = "sms";

    /**
     * URI scheme for telephone number.
     */
    public static final String SCHEME_TEL = "tel";
    /**
     * URI scheme for email address.
     */
    public static final String SCHEME_MAILTO = "mailto";
    /**
     * URI scheme for map address.
     */
    public static final String SCHEME_GEO = "geo";
    private  Activity mActivity;

    public  boolean to(String urlInput) {
        Uri uri = null;
        try {
            uri = Uri.parse(urlInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uri == null) {
            return DEFALT_POLICY;
        }
        if (HTTP_SCHEME.equals(uri.getScheme()) || HTTPS_SCHEME.equals(uri.getScheme())) {
            return false;
        }
        if (handleCommonScheme(uri)) {
            return true;
        }
        if (handleThirdPartScheme(uri)) {
            return true;
        }
        if (handleMyScheme(uri)) {
            return true;
        }

        return DEFALT_POLICY;
    }

    /**
     * 系统功能调用，例如tel：
     * @param uri
     * @return
     */
    private  boolean handleCommonScheme(Uri uri) {
        String schema = uri.getScheme();
        if (SCHEME_TEL.equals(schema) || SCHEME_SMS.equals(schema) || SCHEME_MAILTO.equals(schema) || SCHEME_GEO.equals(schema)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                mActivity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        //see https://developer.chrome.com/multidevice/android/intents
        if (INTENT_SCHEME.equals(schema)) {
            PackageManager packageManager = mActivity.getPackageManager();
            try {
                Intent intent = Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME);
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                // 跳到该应用
                if (info != null) {
                    mActivity.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 第三方，微信等
     * @param uri
     * @return
     */
    private  boolean handleThirdPartScheme(Uri uri) {
        return false;
    }

    /**
     * 自定义
     * @param uri
     * @return
     */
    private  boolean handleMyScheme(Uri uri) {
        return false;
    }
}
