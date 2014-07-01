package edu.vuum.mocca;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * The main Activity that launches the screen users will see.
 */

class GenericServiceConnection<InterfaceType> implements ServiceConnection {
    /**
     * InterfaceFactory defines a factory method that converts the
     * IBinder service into the appropriate AIDL interface object.
     */
    interface InterfaceFactory<InterfaceType> {
        public InterfaceType asInterface(IBinder service);
    }

    /**
     * Reference to the object after the client has finished binding
     * to the Bound Service.
     */
    private InterfaceType mInterface;

    /**
     * Instance that implements the InterfaceFactory interface to
     * return the appropriate AIDL interface object.
     */
    private InterfaceFactory<InterfaceType> mInterfaceFactory;

    /**
     * Accessor that returns the interface.
     */
    public InterfaceType getInterface() {
        return mInterface;
    }

    /**
     * Constructor stores the InterfaceFactory for later use.
     */
    GenericServiceConnection(InterfaceFactory<InterfaceType> i) {
    	super();
        mInterfaceFactory = i;
    }

    /**
     * Hook method called back by the Android Service framework after
     * connection is established to a Bound Service.
     */
    @Override
    public void onServiceConnected(ComponentName name,
                                   IBinder service) {
        Log.d("GenericServiceConnection", "Connected to ComponentName " + name);
        // Cast the returned IBinder object to the InterfaceType and
        // store it for later use in mInterface.
        mInterface = mInterfaceFactory.asInterface(service);
    }

    /**
     * Called if the Bound Service crashes and is no longer
     * available. The ServiceConnection will remain bound, but the
     * Service will not respond to any requests.
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        mInterface = null;
    }
}

