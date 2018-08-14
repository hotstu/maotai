package github.hotstu.maotai.provider;

import android.content.Intent;

/**
 * @author hglf
 * @since 2018/8/10
 */
public interface ActivityResultObserver {
    /**
     *
     * @param reqCode
     * @param resultCode
     * @param data
     * @return true if the result has been consumed  & will NOT be passed to next
     */
    public boolean onActivityResult(int reqCode, int resultCode, Intent data);
}
