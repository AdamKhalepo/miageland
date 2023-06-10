package com.miage.miageland_back.park;

import lombok.Data;

@Data
public class Park {

    /**
     * The unique instance of the class
     */
    private static Park INSTANCE;

    private String name;

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
