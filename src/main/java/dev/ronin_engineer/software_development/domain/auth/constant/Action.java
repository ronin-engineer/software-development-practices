package dev.ronin_engineer.software_development.domain.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Action {


    CREATE("create"),
    DELETE("delete"),
    UPDATE("update"),
    VIEW("view"),
    IMPORT("import"),
    EXPORT("export"),
    APPROVE("approve"),
    ;

    private final String action;
}