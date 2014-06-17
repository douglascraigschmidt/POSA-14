package edu.vuum.mocca;

/**
 * @class GeoNamesCall
 *
 * @brief An AIDL interface used to get the results of a web service
 *        call in another process.  Any invocations of
 *        getWebServiceResultsPathname() will block until the method
 *        completes and returns from the other process.
 *
 *	  This file generates a Java interface in the gen folder.
 */
interface GeoNamesCall {
   /**
    * Invoke a call to the GeoNames Web service at the provided
    * Internet uri and returns results as a string.
    */
    String getWebServiceResults (in Uri uri);
}
