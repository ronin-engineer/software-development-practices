package dev.ronin_engineer.software_development.application.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("SUCCESS", 200),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", 500),
    UNAUTHORIZED("UNAUTHORIZED", 401),
    INVALID_ACCESS_TOKEN("INVALID_ACCESS_TOKEN", 401),
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", 401),
    INVALID_ID_TOKEN("INVALID_ID_TOKEN", 401),
    FORBIDDEN("FORBIDDEN", 403),
    INVALID_REQUEST("INVALID_REQUEST", 400),
    SERVICE_MAINTENANCE("SERVICE_MAINTENANCE", 400),
    BAD_REQUEST("BAD_REQUEST", 400),
    MISSING_FIELD("MISSING_FIELD", 400),
    INVALID_FIELD("INVALID_FIELD", 400),
    ;

    private final String code;
    private final int status;

    @Override
    public String toString() {
        return code;
    }
}
