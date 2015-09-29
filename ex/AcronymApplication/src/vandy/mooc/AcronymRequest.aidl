package vandy.mooc;

import vandy.mooc.AcronymResults;

/**
 * Interface defining the method that the AcronymServiceAsync will
 * implement to provide asynchronous access to the Acronym Web
 * service.
 */
interface AcronymRequest {
   /**
    * A one-way (non-blocking) call to the AcronymServiceAsync that
    * retrieves information about an acronym from the Acronym Web
    * service.  The AcronymServiceAsync subsequently uses the
    * AcronymResults parameter to return a List of AcronymData
    * containing the results from the Web service back to the
    * AcronymActivity.
    */
    oneway void expandAcronym (in AcronymResults results,
                               in String acronym); 
}
