package edu.vuum.mocca;

import edu.vuum.mocca.AcronymCallback;

/**
 * Interface defining the method that the AcronymServiceAsync will
 * implement to provide access to the Acronym Web service.
 */
interface AcronymRequest {
   /**
    * A one-way (non-blocking) call to the AcronymServiceAsync that
    * retrieves information about an acronym from the Acronym Web
    * service.  The AcronymServiceAsync subsequently uses the
    * AcronymCallback parameter to return a List of AcronymData
    * containing the results from the Web service back to the
    * AcronymActivity.
    */
    oneway void callAcronymRequest (in AcronymCallback callback,
                                    in String acronym); 
}
