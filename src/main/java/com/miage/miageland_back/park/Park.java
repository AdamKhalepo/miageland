package com.miage.miageland_back.park;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class Park {

    private static String name = "MiageLand";

    private Integer gauge;
}
