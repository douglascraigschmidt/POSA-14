package edu.vuum.mocca;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;

/**
 * @class EarthQuakeUtils
 *
 * @brief This class encapsulates a static helper method that both
 *        EarthQuakeService* implementations can access without
 *        redefining them in each Service.
 */
public class EarthQuakeUtils {
    private final static String TAG = "GeoNamesUtils";
    
    static private class JSONResponseHandler 
                         implements ResponseHandler<List<EarthQuakeData>> {
    @Override
    public List<EarthQuakeData> handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {
            List<EarthQuakeData> result = new ArrayList<EarthQuakeData>();
            String JSONResponse = new BasicResponseHandler()
                .handleResponse(response);
            try {
                JSONObject object =
                    (JSONObject) new JSONTokener(JSONResponse).nextValue();

                JSONArray earthquakes = object.getJSONArray("earthquakes");
                for (int i = 0; i < earthquakes.length(); i++) {
                    JSONObject tmp = (JSONObject) earthquakes.get(i);
                    result.add(new EarthQuakeData(tmp.getDouble("lat"),
                                                 tmp.getDouble("lng"),
                                                 tmp.getDouble("magnitude")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    /**
     * Invoke a call to the GeoNames webservice at the provided
     * Internet url and return the results as a string.
     *
     * @param uri       the web url
     * 
     * @return          the results obtained from the Web Service call.
     */
    public static List<EarthQuakeData> getEarthQuakeData(AndroidHttpClient client,
                                           Uri uri) {
        String url = uri.toString();
        String regex = "%26(?!\\d)";
        // Replace any "%26" with the "&" so the GeoNames webservice
        // will work properly.
        url = url.replaceAll(regex, "&");

        Log.d(TAG,
              "calling getEarthQuakeData() with URL " 
              + url);

        HttpGet request = new HttpGet(url);
        JSONResponseHandler responseHandler = 
            new JSONResponseHandler();

        try {
            // Get Earthquake data in JSON format, parse data into a
            // List of EarthQuakeDatas, and return the results.
            return client.execute(request,
                                  responseHandler);

        } catch (ClientProtocolException e) {
            Log.i(TAG, "ClientProtocolException");
        } catch (IOException e) {
            Log.i(TAG, "IOException");
        }
        return null;
    }
}
