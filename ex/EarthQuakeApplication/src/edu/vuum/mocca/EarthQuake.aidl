package edu.vuum.mocca;

import edu.vuum.mocca.EarthQuakeData;
import java.util.List;

/**
 * @class EarthQuake
 *
 * @brief An AIDL interface used to get the results of a web service
 *        call in another process.  Any invocations of
 *        getEarthQuakeData() will block until the method completes
 *        and returns from the other process.
 *
 *	  This file generates a Java interface in the gen folder.
 */
interface EarthQuake {
   /**
    * Invoke a call to the GeoNames Web service at the provided
    * Internet uri and returns results as a List of EarthQuakeData.
    */
    List<EarthQuakeData> getEarthQuakeData (in Uri uri);
}
