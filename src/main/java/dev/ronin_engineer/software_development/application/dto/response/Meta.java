package dev.ronin_engineer.software_development.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meta {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("service_id")
    private String serviceId;

    private String code;

    private String type;

    private String message;

    @JsonProperty("extra_meta")
    private Map<String, Object> extraMeta;

}
