package com.paulocaldeira.movies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.paulocaldeira.movies.helpers.FormatHelper;

import java.util.Date;

/**
 * Movie Class
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */
public class Movie implements Parcelable {
    // Attributes
    private long mId;
    private String mTitle;
    private String mSynopsis;
    private String mPosterUrl;
    private String mBackdropUrl;
    private Date mReleaseDate;
    private double mRate;
    private boolean mFavorite = false;

    public Movie() {
        //
    }

    protected Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mSynopsis = in.readString();
        mPosterUrl = in.readString();
        mBackdropUrl = in.readString();
        mReleaseDate = FormatHelper.parseDate(in.readString());
        mRate = in.readDouble();
        mFavorite = in.readByte() != 0;
    }

    public void setId(long id) {
        mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setSynopsis(String synopsis) {
        mSynopsis = synopsis;
    }

    public void setPosterUrl(String posterUrl) {
        mPosterUrl = posterUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        mBackdropUrl = backdropUrl;
    }

    public void setReleaseDate(Date releaseDate) {
        mReleaseDate = releaseDate;
    }

    public void setRate(double rate) {
        mRate = rate;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getBackdropUrl() {
        return mBackdropUrl;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public double getRate() {
        return mRate;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mSynopsis);
        parcel.writeString(mPosterUrl);
        parcel.writeString(mBackdropUrl);
        parcel.writeString(FormatHelper.formatDate(mReleaseDate));
        parcel.writeDouble(mRate);
        parcel.writeByte((byte) (mFavorite ? 1 : 0));
    }

    // Parceable Creator
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static class Builder {
        // Attributes
        private Movie mMovie;

        public Builder() {
            mMovie = new Movie();
        }

        /**
         * Sets movie id
         * @param id Id
         */
        public Builder setId(long id) {
            mMovie.setId(id);
            return this;
        }

        /**
         * Sets movie title
         * @param name Title
         */
        public Builder setTitle(String name) {
            mMovie.setTitle(name);
            return this;
        }

        /**
         * Sets movie synopsis
         * @param synopsis Description
         */
        public Builder setSynopsis(String synopsis) {
            mMovie.setSynopsis(synopsis);
            return this;
        }

        /**
         * Sets movie poster url
         * @param url Poster url
         */
        public Builder setPosterUrl(String url) {
            mMovie.setPosterUrl(url);
            return this;
        }

        /**
         * Sets movie backdrop url
         * @param url Backdrop url
         */
        public Builder setBackdropUrl(String url) {
            mMovie.setBackdropUrl(url);
            return this;
        }

        /**
         * Sets movie release date
         * @param releaseDate Release date
         */
        public Builder setReleaseDate(Date releaseDate) {
            mMovie.setReleaseDate(releaseDate);
            return this;
        }

        /**
         * Sets movie rate
         * @param rate Rate
         */
        public Builder setRate(double rate) {
            mMovie.setRate(rate);
            return this;
        }

        /**
         * Sets if movie is favorite
         * @param favorite Favorite
         */
        public Builder setFavorite(boolean favorite) {
            mMovie.setFavorite(favorite);
            return this;
        }

        /**
         * Returns built movie
         * @return Movie
         */
        public Movie build() {
            return mMovie;
        }
    }
}
