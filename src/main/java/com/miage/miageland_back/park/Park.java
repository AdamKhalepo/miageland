package com.miage.miageland_back.park;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Park {

    private static Park INSTANCE;

    private String name;

    private Integer gauge;

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
