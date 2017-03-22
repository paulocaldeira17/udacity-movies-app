package com.paulocaldeira.movies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.paulocaldeira.movies.helpers.FormatHelper;

import java.util.Date;

/**
 * Movie Review Class
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */
public class MovieReview implements Parcelable {
    // Attributes
    private String mAuthor;
    private String mContent;

    public MovieReview(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }

    protected MovieReview(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
    }

    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel source) {
            return new MovieReview(source);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}
