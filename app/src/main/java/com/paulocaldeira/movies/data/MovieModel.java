package com.paulocaldeira.movies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.paulocaldeira.movies.helpers.FormatHelper;

import java.util.Date;

/**
 * Movie Model Class
 * @author Paulo Caldeira <paulocaldeira17@gmail.com>
 * @created 1/31/17
 */
public class MovieModel implements Parcelable {
    // Attributes
    private String mTitle;
    private String mSynopsis;
    private String mPosterUrl;
    private String mBackdropUrl;
    private Date mReleaseDate;
    private double mRate;

    // Parceable Creator
    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public MovieModel() {
        //
    }

    protected MovieModel(Parcel in) {
        mTitle = in.readString();
        mSynopsis = in.readString();
        mPosterUrl = in.readString();
        mBackdropUrl = in.readString();
        mReleaseDate = FormatHelper.parseDate(in.readString());
        mRate = in.readDouble();
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
        parcel.writeString(mTitle);
        parcel.writeString(mSynopsis);
        parcel.writeString(mPosterUrl);
        parcel.writeString(mBackdropUrl);
        parcel.writeString(FormatHelper.formatDate(mReleaseDate));
        parcel.writeDouble(mRate);
    }

    public static class Builder {
        // Attributes
        private MovieModel mMovie;

        public Builder() {
            mMovie = new MovieModel();
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
         * Returns built movie
         * @return Movie
         */
        public MovieModel build() {
            return mMovie;
        }
    }
}
