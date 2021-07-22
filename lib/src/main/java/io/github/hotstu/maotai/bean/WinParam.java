package io.github.hotstu.maotai.bean;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author hglf
 * @since 2018/7/24
 */
public class WinParam implements Parcelable {
    public String url;
    public String name;
    @Nullable public JSONObject pageParam;

    public WinParam() {
    }

    public WinParam(String name,String url, @Nullable JSONObject pageParam) {
        this.url = url;
        this.name = name;
        this.pageParam = pageParam;
    }

    protected WinParam(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        String json = in.readString();
        if (json != null) {
            try {
                this.pageParam = new JSONObject(json);
            } catch (JSONException e) {
                throw new IllegalArgumentException(e);
            }
        }

    }

    public static final Creator<WinParam> CREATOR = new Creator<WinParam>() {
        @Override
        public WinParam createFromParcel(Parcel in) {
            return new WinParam(in);
        }

        @Override
        public WinParam[] newArray(int size) {
            return new WinParam[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        if (pageParam != null) {
            dest.writeString(pageParam.toString());
        }
    }
}
