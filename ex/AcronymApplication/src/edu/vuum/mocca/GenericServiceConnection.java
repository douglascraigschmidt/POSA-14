package edu.vuum.mocca;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * @class GenericServiceConnection
 *
 * @brief This class provides a generic framework for defining a
 *        ServiceConnection object to an AIDLInterface that resides in
 *        a Bound Service.  It factors out common behavior that is
 *        otherwise written in a tedious and error-prone way for each
 *        ServiceConnection object.  It would be cleaner if Java
 *        generics supported traits and signature-based typing the way
 *        that C++ templates do..
 */
class GenericServiceConnection<AIDLInterface> implements ServiceConnection {
    /**
     * InterfaceFactory defines a factory method that converts the
     * IBinder service into the appropriate AIDL interface object.  We
     * need to use this factory since Java generics lack the traits
     * mechanisms and signature-based typing that C++ templates
     * provide.
     */
    interface InterfaceFactory<AIDLInterface> {
        public AIDLInterface asInterface(IBinder service);
    }

    /**
     * Reference to the AIDL interface object after the client has
     * finished binding to the Bound Service.
     */
    private AIDLInterface mInterface;

    /**
     * Instance that implements the InterfaceFactory interface to
     * return the appropriate AIDL interface object.
     */
    private InterfaceFactory<AIDLInterface> mInterfaceFactory;

    /**
     * Accessor that returns the AIDL interface object.
     */
    public AIDLInterface getInterface() {
        return mInterface;
    }

    /**
     * Constructor stores the InterfaceFactory for later use.
     */
    GenericServiceConnection(InterfaceFactory<AIDLInterface> i) {
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
        // Cast the returned IBinder object to the AIDLInterface and
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

