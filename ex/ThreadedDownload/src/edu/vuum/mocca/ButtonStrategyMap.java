package edu.vuum.mocca;

import java.util.HashMap;

/**
 * @class ButtonStrategyMap
 *
 * @brief Maps buttons (represented via their resource ids) to
 *        ButtonStrategy implementations.
 */
public class ButtonStrategyMap {
    private HashMap<Integer, ButtonStrategy> mButtonStrategyMap = 
        new HashMap<Integer, ButtonStrategy>();
            
    public ButtonStrategyMap(Integer[] buttonIds,
                            ButtonStrategy[] buttonStrategys) {
        // Map buttons pushed by the user to the requested type of
        // ButtonStrategy.
        for (int i = 0; i < buttonIds.length; ++i)
            mButtonStrategyMap.put(buttonIds[i],
                                  buttonStrategys[i]);
    }

    /**
     * Factory method that returns the request ButtonStrategy
     * implementation.
     */
    public ButtonStrategy getButtonStrategy(int buttonId) {
        // Return the designated ButtonStrategy.
        return mButtonStrategyMap.get(buttonId);
    }
}

