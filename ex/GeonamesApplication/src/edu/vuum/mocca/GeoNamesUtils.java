package edu.vuum.mocca;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.net.Uri;
import android.util.Log;

/**
 * @class GeoNamesUtils
 *
 * @brief This class encapsulates a static helper method that both
 *        GeoNamesService* implementations can access without
 *        redefining them in each Service.
 */
public class GeoNamesUtils {
    private final static String TAG = "GeoNamesUtils"; 
    
    /**
     * Invoke a call to the GeoNames webservice at the provided
     * Internet url and return the results as a string.
     *
     * @param uri       the web url
     * 
     * @return          the results obtained from the Web Service call.
     */
    public static String getWebServiceResults(Uri uri) {
        String url = uri.toString();
        String regex = "%26(?!\\d)";
        // Replace any "%26" with the "&" so the GeoNames webservice
        // will work properly.
        url = url.replaceAll(regex, "&");

        Log.d(TAG,
              "calling getWebServiceResults() with URL " 
              + url);
        try {
            // Send a request to the GeoNames Web service.
        	HttpURLConnection conn =
                    (HttpURLConnection) new URL(url).openConnection();

            // Return the results of the Web service as a String.
            return copy(conn.getInputStream());
        } catch (MalformedURLException e1) {
            Log.d(TAG,
                  "MalformedURLException while downloading. Returning null.");
            e1.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG,
                  "Exception while downloading. Returning null.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Copy the contents of an InputStream into a String.
     * 
     * @param is       InputStream
     * @return string  
     */
    private static String copy(final InputStream is) {
        StringBuffer data = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            // Copy all the results from the GeoNames Web service into
            // a String and return it to the caller.
            for (String rawData;
                 (rawData = br.readLine()) != null;
                 )
                data.append(rawData);
        } catch (IOException e) {
            Log.d(TAG,
                  "IOException while downloading. Returning null.");
            e.printStackTrace();
            return null;
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
         	
        return data.toString();
    }
}
