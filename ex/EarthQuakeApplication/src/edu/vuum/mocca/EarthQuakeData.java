package edu.vuum.mocca;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is an extended Plain Old Java Object (POJO), which means
 * that it is only used for data transport within our application.
 * This POJO implements the Parcelable interface to allow Inter
 * Process Communication[ (IPC) between the GeoNamesService* and
 * the GeoNamesActivity.
 * 
 * Parcelable defines an interface for marshaling/de-marshaling
 * https://en.wikipedia.org/wiki/Marshalling_(computer_science)
 * to/from a format that Android uses to allow data transport between
 * processes on a device.
 * 
 * Discussion of the details of Parcelable is outside the scope of
 * this assignment, but you can read more at
 * https://developer.android.com/reference/android/os/Parcelable.html
 */

public class EarthQuakeData implements Parcelable {
    /*
     * These data members are the local variables that will store the
     * EarthQuakeRec's state.
     */

    public double mLat;
    public double mLng;
    public double mMagnitude;

    public EarthQuakeData(double lat, double lng, double magnitude) {
        mLat = lat;
        mLng = lng;
        mMagnitude = magnitude;
    }

    public EarthQuakeData(Parcel in) {
        mLat = in.readDouble();
        mLng = in.readDouble();
        mMagnitude = in.readDouble();
    }

    /**
     * The toString() custom implementation.
     */
    @Override
    public String toString() {
        return "EarthQuakeRec [mLat=" 
            + mLat
            + ", mLng=" 
            + mLng
            + ", mMagnitude=" 
            + mMagnitude
            + "]";
    }

    /*
     * Parcelable related methods.
     */

    /**
     * A bitmask indicating the set of special object types marshaled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Marshal this AcronymData to the target Parcel.
     */
    @Override
    public void writeToParcel(Parcel dest,
                              int flags) {
        // TODO Auto-generated method stub
        dest.writeDouble(mLat);
        dest.writeDouble(mLng);
        dest.writeDouble(mMagnitude);
    }

    /**
     * public Parcelable.Creator for EarthQuakeRec, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<EarthQuakeData> CREATOR =
        new Parcelable.Creator<EarthQuakeData>() {
        public EarthQuakeData createFromParcel(Parcel in) {
            return new EarthQuakeData(in);
        }

        public EarthQuakeData[] newArray(int size) {
            return new EarthQuakeData[size];
        }
    };
}
