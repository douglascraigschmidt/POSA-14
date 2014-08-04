package edu.vuum.mocca;

import java.util.List;

import android.app.Activity;

/**
 * This class is used the base class for the EarthQuakeActivity.  It
 * instantiates the UI and handles displaying images and getting text
 * from the EditText object by making displayBitmap() and
 * getUrlString() available to subclasses.  This design separates
 * concerns by having EarthQuakeBase handle UI functionality while
 * subclasses (such as EarthQuakeActivity) handle any Service-related
 * communication with the EarthQuake Web service.
 * 
 * EarthQuakeBase is an example of the Template Method pattern since it
 * extends Activity and overrides its onCreate() hook method.  More
 * generally, any object that extends Activity and overrides its hook
 * methods, such as onStart() or onPause(), is also an example of the
 * Template Method pattern.
 */
public class EarthQuakeBaseActivity extends Activity {
    /**
     * Used for debugging.
     */
    private final String TAG = this.getClass().getSimpleName(); 

    /**
     * Coordinates used for centering the Map.
     */
    private static final double CAMERA_LNG = 87.0;
    private static final double CAMERA_LAT = 17.0;

    /**
     * Display the given result string in the TextView.
     */
    protected void displayResults(List<EarthQuakeData> earthQuakeDataList) {
        // Get Map Object
        GoogleMap mMap = 
            ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        if (null != mMap) {
            // Add a marker for every earthquake.

            for (EarthQuakeData rec : result) {

                // Add a new marker for this earthquake
                mMap.addMarker
                    (new MarkerOptions()
                     // Set the Marker's position
                     .position(new LatLng(rec.mLat, rec.mLng))
                     
                     // Set the title of the Marker's information window
                     .title(String.valueOf(rec.mMagnitude))

                     // Set the color for the Marker
                     .icon(BitmapDescriptorFactory
                           .defaultMarker(getMarkerColor(rec.mMagnitude))));

            }

            // Center the map.  Should compute map center from the
            // actual data.
            mMap.moveCamera(CameraUpdateFactory.newLatLng
                            (new LatLng(CAMERA_LAT,
                                        CAMERA_LNG)));

        }
        // if (null != mClient)
        // mClient.close();
    }
    
    /**
     * Assign marker color.
     */
    private float getMarkerColor(double magnitude) {
        if (magnitude < 6.0) {
            magnitude = 6.0;
        } else if (magnitude > 9.0) {
            magnitude = 9.0;
        }

        return (float) (120 * (magnitude - 6));
    }

}
