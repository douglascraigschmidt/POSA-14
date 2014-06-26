package edu.vuum.mocca;

import edu.vuum.mocca.AcronymData;
import java.util.List;

/**
 * Interface defining the method that receives callbacks from the
 * AcronymServiceAsync.
 */
interface AcronymResults {
    /**
     * This one-way (non-blocking) method allows the
     * AcyronymServiceAsync to return the List of AcronymData results
     * associated with a one-way AcronymRequest.callAcronymRequest()
     * call.
     */
    oneway void sendResults(in List<AcronymData> results);
}
