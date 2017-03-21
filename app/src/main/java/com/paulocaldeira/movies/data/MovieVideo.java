package com.paulocaldeira.movies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movie Video Class
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */
public class MovieVideo implements Parcelable {
    // Attributes
    private String mName;
    private String mType;
    private String mKey;

    public MovieVideo(String name, String type, String key) {
        mName = name;
        mType = type;
        mKey = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mType);
        dest.writeString(this.mKey);
    }

    protected MovieVideo(Parcel in) {
        this.mName = in.readString();
        this.mType = in.readString();
        this.mKey = in.readString();
    }

    public static final Creator<MovieVideo> CREATOR = new Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel source) {
            return new MovieVideo(source);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };
}
