package dev.ronin_engineer.software_development.domain.airport.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Airport {

    private String airportId;

    private String airportName;

    private String data;

    private int status;

    private long version;

    private Date createdAt;

    private String createdBy;

    private Date updatedAt;

    private String updatedBy;
}
