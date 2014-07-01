package edu.vuum.mocca;

import android.os.IBinder;

/**
 * Factory Class to build the GenergicServiceConnection(s) desired,
 * moving the code to this file does nothing but add to overall
 * readability of AcronymActivity.
 */
public class AcronymServiceConnectionFactory {
    /**
     * Create the AcronymRequest GenericServiceConnection.
     * @return the GenericServiceConnection to use.
     */
    static public GenericServiceConnection<AcronymRequest> newAcronymRequestConnection() {
        return new GenericServiceConnection<AcronymRequest>
            (new GenericServiceConnection.InterfaceFactory<AcronymRequest>() {
                public AcronymRequest asInterface(IBinder service) {
                    return AcronymRequest.Stub.asInterface(service);
                }
            });
    }

    /**
     * Create the AcronymCall GenericServiceConnection.
     * @return the GenericServiceConnection to use.
     */
    static public GenericServiceConnection<AcronymCall> newAcronymCallConnection() {
        return new GenericServiceConnection<AcronymCall>
            (new GenericServiceConnection.InterfaceFactory<AcronymCall>() {
                public AcronymCall asInterface(IBinder service) {
                    return AcronymCall.Stub.asInterface(service);
                }
            });
    }
}
