package dev.ronin_engineer.software_development.domain.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Resource {


    AIRPORTS("airports"),
    ;

    private final String resource;
}