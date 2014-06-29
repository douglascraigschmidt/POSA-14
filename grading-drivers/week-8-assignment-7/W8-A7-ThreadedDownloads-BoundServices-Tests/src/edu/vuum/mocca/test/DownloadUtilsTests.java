package edu.vuum.mocca.test;

import java.io.IOException;

import android.content.Context;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import edu.vuum.mocca.DownloadActivity;
import edu.vuum.mocca.DownloadUtils;

/**
 * @class DownloadUtilsTests
 *
 * @brief Test the functionality of the DownloadUtils class
 */
public class DownloadUtilsTests extends  ActivityInstrumentationTestCase2<DownloadActivity> {
    /**
     * Constructor initializes the superclass
     */
    public DownloadUtilsTests () {
        super (DownloadActivity.class);
    }
	
    /**
     * Try downloading a file
     * @throws IOException 
     */
    public void test_downloadFile () throws IOException {
        Context context = getInstrumentation().getContext();
        String result = DownloadUtils.downloadFile(getActivity(), 
                                                   Uri.parse(Options.TEST_URI));
	
        assertTrue(Utilities.checkDownloadedImage(context, result));
    }
	
}
