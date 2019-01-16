package github.hotstu.maotai.engine;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;

import github.hotstu.maotai.UI;
import github.hotstu.maotai.bean.WinParam;
import github.hotstu.maotai.provider.ActivityResultObserver;
import github.hotstu.naiue.arch.MOFragment;

/**
 * 一个Fragment包含一个root Frame，称为一个Window，这个和android里的window没有关系，是一个自定义的概念
 * 一个Window可以包含多个Frame，每一个Frame都有一个name
 *
 * @author hglf
 * @since 2018/7/23
 */
public class CoreFragment extends MOFragment {
    public static final String ROOT_WINDOW_TAG = "root";
    public static final String ROOT_FRAME_TAG = "rootFrame";
    private WinParam winParam;
    private JsBridgeView container;
    private SparseArray<ActivityResultObserver> observerSparseArray;


    public static CoreFragment newInstance(WinParam winParam) {
        CoreFragment f = new CoreFragment();
        Bundle b = new Bundle();
        b.putParcelable("winParam", winParam);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ensureWinParams();
    }

    public UI getUI() {
        return (UI) getActivity();
    }

    @Override
    protected View onCreateView() {
        if (getContext() == null) {
            throw new IllegalStateException("getContext() == null");
        }

        container = new JsBridgeView(this, winParam);
        container.setFrameName(ROOT_FRAME_TAG);
        return container;
    }

    public JsBridgeView getContainer() {
        return container;
    }

    @Override
    public String getTagNmae() {
        ensureWinParams();
        return winParam.name;
    }

    public boolean onKeyPressed(int keyCode, KeyEvent event) {
        return container != null && container.onKeyPressed(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (observerSparseArray != null && observerSparseArray.get(requestCode) != null) {
            observerSparseArray.get(requestCode).onActivityResult(requestCode, resultCode, data);
            observerSparseArray.put(requestCode, null);
        }
    }

    public void registerActivityResultObserver(int requestCode, ActivityResultObserver observer) {
        if (observerSparseArray == null) {
            observerSparseArray = new SparseArray<>();
        }
        observerSparseArray.put(requestCode, observer);
    }

    public boolean onBackPressed() {
        return container != null && container.onBackPressed();
    }

    private void ensureWinParams() {
        if (winParam == null) {
            if (getArguments() == null) {
                throw new IllegalArgumentException("getArguments == null");
            }
            WinParam winParam = getArguments().getParcelable("winParam");
            if (winParam == null) {
                throw new IllegalArgumentException("winParam == null");
            }
            this.winParam = winParam;
        }
    }


    @Override
    public void startFragment(MOFragment fragment) {
        super.startFragment(fragment);
    }


    @Override
    public void onDestroy() {
        observerSparseArray = null;
        getContainer().destory();
        super.onDestroy();
    }
}
