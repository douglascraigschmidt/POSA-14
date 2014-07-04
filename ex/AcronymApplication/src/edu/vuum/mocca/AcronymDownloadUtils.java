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

import android.net.http.AndroidHttpClient;
import android.util.Log;

/**
 * @class AcronymDownloadUtils
 *
 * @brief Handles the actual downloading of Acronym information from
 *        the GeoNames Web service.
 */
public class AcronymDownloadUtils {
    // Logging Tag
    private final static String TAG = AcronymDownloadUtils.class
        .getCanonicalName();

    /** 
     * URL to the Acronym web service.
     */
    private final static String ACRONYM_URL =
        "http://www.nactem.ac.uk/software/acromine/dictionary.py?sf=";

    /**
     * A Handler that encapsulates the process of generating a
     * response object from a HttpResponse.
     */
    static class JSONResponseHandler implements ResponseHandler<List<AcronymData>> {
        /**
         * This factory method converts the HttpResponse received from
         * the GeoNames Web service into a List of AcronymData.
         */
        @Override
        public List<AcronymData> handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {

            // Stores the processed results we get back from the
            // GeoNames Web service.
            List<AcronymData> result = new ArrayList<AcronymData>();

            // A ResponseHandler that returns the response body as a
            // String for successful (2xx) responses.
            String JSONResponse =
                new BasicResponseHandler().handleResponse(response);

            try {
                // Takes a JSON source string and extracts characters
                // and tokens from it.
                JSONArray object =
                    (JSONArray) new JSONTokener(JSONResponse).nextValue();

                // Get the JSON array of acronym results, marked with
                // the 'lfs' name.
                final JSONArray options = object.getJSONObject(0).getJSONArray("lfs");

                // For each option, create an AcronymData and add it
                // to rValue.
                for (int i = 0; i < options.length(); i++) {
                    JSONObject jsonObject = options.getJSONObject(i);
                    result.add(new AcronymData(jsonObject.getString("lf"),
                                               jsonObject.getInt("freq"),
                                               jsonObject.getInt("since")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    /**
     * Obtain the Acronym information.
     * 
     * @return The information that responds to your current acronym search.
     */
    public static List<AcronymData> getResults(AndroidHttpClient client,
                                               final String acronym) {
        // Object that encapsulates the HTTP GET request to the
        // Acronym Web service.
        HttpGet request = new HttpGet(ACRONYM_URL + acronym);

        // Factory method that converts the JSON string returned from
        // the Acronym Web service into a List of AcronymData.
        JSONResponseHandler responseHandler = 
            new JSONResponseHandler();

        try {
            // Get expanded acronyms from the Web service in JSON
            // format, parse data into a List of AcronymData, and
            // return the results.
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
