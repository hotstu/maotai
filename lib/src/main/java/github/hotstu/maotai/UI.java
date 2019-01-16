package github.hotstu.maotai;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import github.hotstu.maotai.bean.WinParam;
import github.hotstu.maotai.engine.CoreFragment;
import github.hotstu.maotai.engine.MDJsBridgeBuilder;
import github.hotstu.maotai.provider.Injection;
import github.hotstu.maotai.provider.UIViewModel;
import github.hotstu.naiue.arch.MOFragment;
import github.hotstu.naiue.arch.MOFragmentActivity;

public abstract class UI extends MOFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiOnCreate(savedInstanceState);
    }

    protected void UiOnCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            UIViewModel vm = ViewModelProviders.of(this, Injection.getViewModelFactory(this)).get(UIViewModel.class);
            vm.getMdConfigLiveData().observe(this,  mdConfig -> {
                assert mdConfig != null;
                WinParam p = new WinParam();
                p.name = CoreFragment.ROOT_WINDOW_TAG;
                p.url = mdConfig.defaultSrc;
                startFragment(CoreFragment.newInstance(p));
            });
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        CoreFragment fragment = (CoreFragment) getCurrentFragment();
        if (fragment != null && fragment.onKeyPressed(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        CoreFragment fragment = (CoreFragment) getCurrentFragment();
        if (fragment != null) {
            if(!fragment.onBackPressed()) {
                popBackStack();
            }
        }
    }

    public CoreFragment getCoreFragmentByTag(String tag) {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(tag);
        if(fragmentByTag !=  null && fragmentByTag instanceof CoreFragment) {
            return (CoreFragment) fragmentByTag;
        } else {
            return null;
        }
    }

    public abstract List<MDJsBridgeBuilder.JavaInterfaceFactory> getJavaInterfaceFactory();
    public abstract WebChromeClient getCustomWebChromeClient();
    public abstract WebViewClient getCustomWebViewClient();
    public abstract String getCustomUserAgentString();
    public abstract String appVersion();
    public int getDefaultSourceType() {
        return 0;
    }

    public List<CoreFragment> getAllCoreFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        List<CoreFragment> coreFragmentList = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof CoreFragment) {
                coreFragmentList.add((CoreFragment) fragments.get(i));
            }
        }
        return coreFragmentList;
    }

    @Override
    public void popBackStack() {
        popBackStack(null, 0);
    }

    @Override
    public void popBackStack(String tagName) {
        popBackStack(tagName, 0);
    }

    @Override
    public void popBackStackInclusive(String tagName) {
        popBackStack(tagName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    private void popBackStack(String tagName, int flags) {
        MOFragment fragment = getCurrentFragment();
        if (fragment == null) {
            finish();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1|| CoreFragment.ROOT_WINDOW_TAG.equals(tagName)) {
            MOFragment.TransitionConfig transitionConfig = fragment.onFetchTransitionConfig();
            Object toExec = fragment.onLastFragmentFinish();
            if (toExec != null) {
                if (toExec instanceof MOFragment) {
                    MOFragment mFragment = (MOFragment) toExec;
                    startFragment(mFragment);
                } else if (toExec instanceof Intent) {
                    Intent intent = (Intent) toExec;
                    finish();
                    startActivity(intent);
                    overridePendingTransition(transitionConfig.popenter, transitionConfig.popout);
                } else {
                    throw new Error("can not handle the result in onLastFragmentFinish");
                }
            } else {
                finish();
                overridePendingTransition(transitionConfig.popenter, transitionConfig.popout);
            }
        } else {
            getSupportFragmentManager().popBackStackImmediate(tagName, flags);
        }

    }


}
