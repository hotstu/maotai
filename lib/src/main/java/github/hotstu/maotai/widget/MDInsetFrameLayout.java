package github.hotstu.maotai.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import github.hotstu.naiue.util.MORelayInsetsToChild;
import github.hotstu.naiue.widget.InsetsAwareView;


/**
 * @author hglf
 * @since 2018/7/19
 */
public class MDInsetFrameLayout extends FrameLayout implements InsetsAwareView {
    public MDInsetFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }



    public MDInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MDInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        ViewCompat.setOnApplyWindowInsetsListener(this, new MORelayInsetsToChild(true));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
