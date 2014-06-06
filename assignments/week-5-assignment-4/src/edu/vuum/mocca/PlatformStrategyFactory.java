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
	public final static String ANDROID_PLATFORM = "The Android Project";
	public final static String JAVA_V6_PLATFORM = "Sun Microsystems Inc.";
	public final static String JAVA_V7_PLATFORM = "Oracle Corporation";
	public final static String JAVA_V8_PLATFORM = "Oracle Corporation";	
	
    /** 
     * This interface uses the Strategy pattern to create @a PlatformStrategy
     * implementations at runtime.
     */
    private static interface IPlatformStrategyFactoryStrategy 
    {
        public PlatformStrategy execute();
    }
	
    /**
     * HashMap used to map strings containing the Java platform names
     * and dispatch the execute() method of the associated @a PlatformStrategy
     * implementation.
     */
    private HashMap<String, IPlatformStrategyFactoryStrategy> mPlatformStrategyMap = 
        new HashMap<String, IPlatformStrategyFactoryStrategy>();
	
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
        mPlatformStrategyMap.put(ANDROID_PLATFORM,
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
        mPlatformStrategyMap.put("Sun Microsystems Inc.",
                                 new IPlatformStrategyFactoryStrategy() 
                                 {
                                     public PlatformStrategy execute() 
                                     {
                                         return new ConsolePlatformStrategy(output);
                                     }
                                 });

    	/** 
         * The "Oracle Corporation" string maps to a command object
         * that creates an @a ConsolePlatformStrategy implementation.
         */
        mPlatformStrategyMap.put("Oracle Corporation", 
                                 new IPlatformStrategyFactoryStrategy() 
                                 {
                                     public PlatformStrategy execute() 
                                     {
                                         return new ConsolePlatformStrategy(output);
                                     }
                                 });
    }

    /** 
     * Create a new @a PlatformStrategy object based on underlying Java
     * platform.
     */
    public PlatformStrategy makePlatformStrategy() 
    {
        String name = System.getProperty("java.specification.vendor");

        return mPlatformStrategyMap.get(name).execute();
    }
}
