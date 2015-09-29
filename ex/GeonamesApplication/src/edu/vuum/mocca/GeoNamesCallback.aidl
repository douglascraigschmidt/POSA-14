package edu.vuum.mocca;

/**
 * @class
 *
 * @brief An AIDL Interface used to receive results from a prior call
 *        to GeoNamesRequest.callWebService().
 *
 *	  This file generates a Java interface in the gen folder.
 */
interface GeoNamesCallback {
    /**
     * Send a string containing the results from the call to the Web
     * service back to the client.
     */
    oneway void sendResults (in String results);
}
