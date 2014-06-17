package edu.vuum.mocca;

import edu.vuum.mocca.GeoNamesCallback;

/**
 * @class GeoNamesRequest
 *
 * @brief An AIDL interface used to get the results of a web service
 *        call in another process.  The caller provides a
 *        GeoNamesCallback object so that the Service process can
 *        return a result across process boundaries asynchronously.
 *
 *	  This file generates a Java interface in the gen folder.
 */
interface GeoNamesRequest {
   /**
    * Invoke a call to the GeoNames webservice at the provided
    * Internet uri.  The Service uses the GeoNamesCallback parameter
    * to return a string containing the results back to the Activity.
    */
    oneway void callWebService (in Uri uri,
                                in GeoNamesCallback callback); 
}
