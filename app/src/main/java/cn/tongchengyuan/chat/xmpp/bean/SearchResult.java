package cn.tongchengyuan.chat.xmpp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 王宗文 on 2016/6/21.
 */
public class SearchResult implements Parcelable {
    public String userName;
    public String nickName;
    public String email;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.nickName);
        dest.writeString(this.email);
    }

    public SearchResult() {
    }

    protected SearchResult(Parcel in) {
        this.userName = in.readString();
        this.nickName = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<SearchResult> CREATOR = new Parcelable.Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel source) {
            return new SearchResult(source);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };
}
