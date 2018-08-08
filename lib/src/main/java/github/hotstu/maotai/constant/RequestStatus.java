package github.hotstu.maotai.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static github.hotstu.maotai.constant.StatusCodes.*;

/**
 * @author hglf
 * @since 2018/8/7
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@IntDef({REQ_STAT_PROGRESS, REQ_STAT_SUCCESS, REQ_STAT_FAILED})
public @interface RequestStatus {}
