package github.hotstu.maotai.provider;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import github.hotstu.labo.rxfetch.Transformers;
import github.hotstu.maotai.engine.MDConfig;
import io.reactivex.Observable;

/**
 * @author hglf
 * @since 2018/8/6
 */
public class UIViewModel extends AndroidViewModel {
    private final String ua;
    private final int defaultSourceType;
    private MutableLiveData<MDConfig> mdConfigLiveData;
    private final Gson g;

    public UIViewModel(@NonNull Application application, String ua, int defaultSourceType ) {
        super(application);
        g = Injection.getGson();
        this.ua = ua;
        this.defaultSourceType = defaultSourceType;
    }

    public MutableLiveData<MDConfig> getMdConfigLiveData() {
        if (mdConfigLiveData == null) {
            mdConfigLiveData = new MutableLiveData<>();
            Observable.<MDConfig>create(emitter -> {
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences(MDConfig.TAG, Context.MODE_PRIVATE);
                int soureType = sharedPreferences.getInt(MDConfig.TAG_SOURETYPE, defaultSourceType);
                MDConfig config = new MDConfig(soureType);
                config.userAgent = ua;
                config.appendCustomSettings(getApplication(), g);
                emitter.onNext(config);
            })
            .compose(Transformers.io_main_ob())
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
