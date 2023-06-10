package com.miage.miageland_back.park;

import lombok.Data;

@Data
/**
 * This singleton class is used to handle the park settings (such as the gauge)
 */
public class Park {

    /**
     * The unique instance of the class
     */
    private static Park INSTANCE;

    /**
     * The name of the park
     */
    private String name;

    /**
     * The maximum number of people allowed in the park on the same day
     */
    private Integer gauge;

    /**
     * Get the instance of the singleton, if it doesn't exist, create it
     * @return the instance of the singleton {@link Park}
     */
    public static Park getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Park("MiageLand");
        }
        return INSTANCE;
    }

    private Park (String name) {
        this.name = name;
    }
}
