package github.hotstu.maotaidemo;

import android.app.Application;
import android.content.Context;

import github.hotstu.labo.tool.ActivityLifeCircleDumper;

/**
 * @author hglf
 * @since 2018/7/25
 */
public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //registerActivityLifecycleCallbacks(new ActivityLifeCircleDumper());
    }
}
