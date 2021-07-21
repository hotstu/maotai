package github.hotstu.maotai.engine;

import android.content.Context;
import android.graphics.Color;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import github.hotstu.maotai.bean.MDConfigMapping;
import github.hotstu.maotai.provider.Injection;



public class MDConfig {
    public static final String TAG = "mdconfig";
    public static final String TAG_SOURETYPE = "soureType";

    public String backgroundColor = "#fff";
    public boolean translucentStatusbar = true;
    public String defaultSrc = "wgt:///page.html";
    public transient String userAgent;
    private final MDSourceType sourceType;
    private final Context context;

    public MDConfig(Context context, int sourceType) {
        this.sourceType = MDSourceType.fromValue(sourceType);
        this.context = context;
        appendCustomSettings(context, Injection.getGson());
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
            if (uri.getScheme() == null || "wgt".equals(uri.getScheme())) {
                String path1 = uri.getPath();
                if (!path1.startsWith("/")) {
                    path1 = "/" + path1;
                }

                URI file = new URI("file", "", sourceType.getSourcePath(context) + path1, null).normalize();
                return file.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "file://" + sourceType.getSourcePath(context) + "/404.html";
    }

    private void appendCustomSettings(Context application, Gson g) {
        InputStreamReader open = null;
        try {
            if (sourceType == MDSourceType.ASSETS) {
                open = new InputStreamReader(application.getAssets().open("widget/config.json"));
            } else {
                URI uri = URI.create(getRealPath("config.json"));
                open = new InputStreamReader(new FileInputStream(new File(uri)));
            }
            MDConfigMapping configMapping = g.fromJson(open, MDConfigMapping.class);
            backgroundColor = configMapping.backgroundColor;
            defaultSrc = configMapping.defaultSrc;
            translucentStatusbar = configMapping.translucentStatusbar;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (open != null) {
                    open.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
