package edu.vuum.mocca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

/**
 * A class to handle the Actual Downloading of information from the
 * Internet.
 */
public class DownloadUtils {
    // Logging Tag
    private final static String LOG_TAG = DownloadUtils.class
        .getCanonicalName();

    /** 
     * URL to the Acronym web service.
     */
    private final static String ACRONYM_URL =
        "http://www.nactem.ac.uk/software/acromine/dictionary.py?sf=";

    /**
     * Obtain the Acronym information.
     * 
     * @return The information that responds to your current acronym search.
     */
    public static List<AcronymData> getResults(final String acronym) {
        // Create the return value, which is a list of AcronymData
        // objects.
        final List<AcronymData> rValue =
            new ArrayList<AcronymData>();

        // Try to access the Internet and parse the JSON reply.
        try {
            // Go the URL and read the JSON array from it, using the
            // readJsonArrayFromUrl() helper method.
            final JSONArray obj = readJsonArrayFromUrl(ACRONYM_URL + acronym);

            // Get the JSON array of results, marked with the 'lfs'
            // name.
            final JSONArray options = obj.getJSONObject(0).getJSONArray("lfs");

            // For each option, create an AcronymData and add it to
            // rValue.
            for (int i = 0; i < options.length(); i++) {
                rValue.add(new AcronymData(options.getJSONObject(i)));
            }

            // Log for debugging purposes.
            Log.d(LOG_TAG, "" + obj.getJSONObject(0).getJSONArray("lfs").length());

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSONException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException: " + e.getMessage());
        }

        return rValue;
    }

    /**
     * Helper method that reads a URL and returns a JSONArray of the
     * data, which only works on a URL to a Web service that returns
     * JSON in an array format.
     * 
     * No defensive programming in this exmaple, for brevity, in
     * production code you would do a lot more checks, etc.
     */
    public static JSONArray readJsonArrayFromUrl(String url)
        throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            // Create a BufferedReader containing the contents
            // downloaded from the Acronym Web service.
            BufferedReader rd =
                new BufferedReader(new InputStreamReader
                                   (is,
                                    Charset.forName("UTF-8")));

            // Convert the java.io.Reader parameter to a String.
            String jsonText = readAll(rd);

            // Create a JSONarray from the String.
            return new JSONArray(jsonText);
        } finally {
            is.close();
        }
    }

    /**
     * Convert the java.io.Reader parameter to a String.
     * 
     * Helper Method for readJsonArrayFromUrl.
     * 
     * @param rd
     *            java.io.Reader to read from
     * 
     * @return String composed of the characters from the Reader.
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
