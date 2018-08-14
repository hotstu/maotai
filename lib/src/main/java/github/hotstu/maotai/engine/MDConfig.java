package github.hotstu.maotai.engine;

import android.app.Application;
import android.graphics.Color;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URI;

import github.hotstu.maotai.bean.MDConfigMapping;
import okhttp3.internal.Util;

/**
 * @author hglf
 * @since 2018/7/24
 */
public class MDConfig {
    public static final String TAG = "mdconfig";
    public static final String TAG_SOURETYPE = "soureType";

    public String backgroundColor = "#fff";
    public boolean translucentStatusbar = true;
    public String defaultSrc = "wgt:///page.html";
    public transient String userAgent;
    public transient MDSourceType sourceType;

    public MDConfig(int sourceType) {
        this.sourceType = MDSourceType.fromValue(sourceType);
    }

    public int getParsedColor() {
        int i;
        try {
            i = Color.parseColor(backgroundColor);
        } catch (Exception e) {
            i = Color.WHITE;
        }
        return i;
    }


    public String getRealPath(String path) {
        try {
            URI uri = URI.create(path);
            if (uri.getScheme() == null|| "wgt".equals(uri.getScheme())) {
                String path1 = uri.getPath();
                if(!path1.startsWith("/")) {
                    path1 = "/" + path1;
                }

                URI file = new URI("file", "", sourceType.getSourcePath() + path1, null).normalize();
                return file.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "file://" + sourceType.getSourcePath() + "/404.html";
    }

    public void appendCustomSettings(Application application, Gson g) {
        InputStreamReader open = null;
        try {
            open = new InputStreamReader(application.getAssets().open("widget/config.json"));
            MDConfigMapping configMapping = g.fromJson(open, MDConfigMapping.class);
            backgroundColor = configMapping.backgroundColor;
            defaultSrc = configMapping.defaultSrc;
            translucentStatusbar = configMapping.translucentStatusbar;
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            Util.closeQuietly(open);
        }
    }
}
