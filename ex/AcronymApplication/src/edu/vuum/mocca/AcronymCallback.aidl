package edu.vuum.mocca;

import edu.vuum.mocca.AcronymData;
import java.util.List;

/**
 * Interface defining the methods that receive callbacks from the
 * remote service.
 */
interface AcronymCallback {
    /**
     * This one-way (non-blocking) method allows the
     * AcyronymServiceAsync to return the List of AcronymData results
     * associated with a one-way AcronymRequest.callAcronymRequest()
     * call.
     */
    oneway void sendResults(in List<AcronymData> results);
}
