package edu.vuum.mocca;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is an extended Plain Old Java Object (POJO), which means
 * that it is only used for data transport within our application.
 * This POJO implements the Parcelable interface to allow Inter
 * Process Communication[ (IPC) between the AcronymServiceAsync and
 * the AcronymActivity.
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
public class AcronymData implements Parcelable {
    /*
     * These data members are the local variables that will store the
     * AcronymData's state
     */

    /**
     * The long form of the acronym (spelled out version).
     */
    String mLongForm;

    /**
     * The relative frequency of usage in print, of this meaning of
     * the acronym.
     */
    int mFreq;

    /**
     * The year the acronym was added to this database of acronyms, or
     * was originally termed.
     */
    int mSince;

    /**
     * Constructor that takes a JSON Object to construct the object
     * (in the JSON format that the Acronym website provides).
     */
    public AcronymData(JSONObject jsonObject) throws JSONException {
        mLongForm = jsonObject.getString("lf");
        mFreq = jsonObject.getInt("freq");
        mSince = jsonObject.getInt("since");
    }

    /**
     * The toString() custom implementation.
     */
    @Override
    public String toString() {
        return "AcronymData [mLongForm=" 
            + mLongForm 
            + ", mFreq=" 
            + mFreq
            + ", mSince=" 
            + mSince 
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
        dest.writeString(mLongForm);
        dest.writeInt(mFreq);
        dest.writeInt(mSince);
    }

    /**
     * Private Constructor for use by CREATOR interface, which is used
     * to de-marshal an AcronymData from the Parcel of data.
     */
    private AcronymData(Parcel in) {
        mLongForm = in.readString();
        mFreq = in.readInt();
        mSince = in.readInt();
    }

    /**
     * public Parcelable.Creator for AcronymData, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<AcronymData> CREATOR =
        new Parcelable.Creator<AcronymData>() {
        public AcronymData createFromParcel(Parcel in) {
            return new AcronymData(in);
        }

        public AcronymData[] newArray(int size) {
            return new AcronymData[size];
        }
    };
}
