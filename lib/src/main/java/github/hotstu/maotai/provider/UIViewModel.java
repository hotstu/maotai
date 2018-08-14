package github.hotstu.maotai.provider;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import github.hotstu.labo.rxfetch.LaboSchedulers;
import github.hotstu.maotai.UI;
import github.hotstu.maotai.engine.MDConfig;
import io.reactivex.Observable;

/**
 * @author hglf
 * @since 2018/8/6
 */
public class UIViewModel extends AndroidViewModel {
    private final UI activity;
    private MutableLiveData<MDConfig> mdConfigLiveData;
    private final Gson g;

    public UIViewModel(@NonNull Application application, UI activity) {
        super(application);
        g = new GsonBuilder().create();
        this.activity = activity;
    }

    public MutableLiveData<MDConfig> getMdConfigLiveData() {
        if (mdConfigLiveData == null) {
            mdConfigLiveData = new MutableLiveData<>();
            Observable.<MDConfig>create(emitter -> {
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences(MDConfig.TAG, Context.MODE_PRIVATE);
                int soureType = sharedPreferences.getInt(MDConfig.TAG_SOURETYPE, 0);
                MDConfig config = new MDConfig(soureType);
                config.userAgent = activity.getCustomUserAgentString();
                config.appendCustomSettings(getApplication(), g);
                emitter.onNext(config);
            })
            .compose(LaboSchedulers.io_main_ob())
            .subscribe(mdConfig -> {
                mdConfigLiveData.setValue(mdConfig);
            }, Throwable::printStackTrace);

        }
        return mdConfigLiveData;
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     * <p>
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
