package edu.vuum.mocca;

import java.util.HashMap;

/**
 * @class PlatformStrategyFactory
 * 
 * @brief This class is a factory that is responsible for building the
 *        designated @a PlatformStrategy implementation at runtime.
 */
public class PlatformStrategyFactory 
{
    /** 
     * This interface uses the Strategy pattern to create @a
     * PlatformStrategy implementations at runtime.
     */
    private static interface IPlatformStrategyFactoryStrategy 
    {
        public PlatformStrategy execute();
    }
	
    /**
     * Enumeration distinguishing platforms Android from plain ol' Java.
     */
    public enum PlatformType {
    	ANDROID,
    	PLAIN_JAVA
    }
    
    /**
     * HashMap used to map strings containing the Java platform names
     * and dispatch the execute() method of the associated @a PlatformStrategy
     * implementation.
     */
    private HashMap<PlatformType, IPlatformStrategyFactoryStrategy> mPlatformStrategyMap = 
        new HashMap<PlatformType, IPlatformStrategyFactoryStrategy>();
	
    /** 
     * Ctor that stores the objects that perform output for a
     * particular platform, such as ConsolePlatformStrategy or the
     * AndroidPlatformStrategy.
     */
    public PlatformStrategyFactory(final Object output,
                                   final Object activity) 
    {
    	/** 
         * The "The Android Project" string maps to a command object
         * that creates an @a AndroidPlatformStrategy implementation.
         */
        mPlatformStrategyMap.put(PlatformType.ANDROID,
                                 new IPlatformStrategyFactoryStrategy() 
                                 {
                                     /** 
                                      * Receives the three parameters, input
                                      * (EditText), output (TextView), activity
                                      * (activity).
                                      */
                                     public PlatformStrategy execute() 
                                     {
                                         return new AndroidPlatformStrategy(output,
                                                                            activity);
                                     }
                                 });
	
    	/** 
         * The "Sun Microsystems Inc." string maps to a command object
         * that creates an @a ConsolePlatformStrategy implementation.
         */
        mPlatformStrategyMap.put(PlatformType.PLAIN_JAVA,
                                 new IPlatformStrategyFactoryStrategy() 
                                 {
                                     public PlatformStrategy execute() 
                                     {
                                         return new ConsolePlatformStrategy(output);
                                     }
                                 });
    }

    /** 
     * Returns the name of the platform in a string. e.g., Android or
     * a JVM.
     */
    public static String platformName() 
    {
        return System.getProperty("java.specification.vendor");
    }
    
    /** 
     * Returns the type of the platformm e.g. Android or
     * a JVM.
     */
    public static PlatformType platformType() {
    	if(platformName().indexOf("Android") >= 0) 
            return PlatformType.ANDROID;
    	else
            return PlatformType.PLAIN_JAVA;
    }

    /** 
     * Create a new @a PlatformStrategy object based on underlying Java
     * platform.
     */
    public PlatformStrategy makePlatformStrategy() 
    {
        PlatformType type = platformType();

        return mPlatformStrategyMap.get(type).execute();
    }
}
