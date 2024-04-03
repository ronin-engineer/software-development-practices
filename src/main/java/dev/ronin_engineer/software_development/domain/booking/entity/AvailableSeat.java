package dev.ronin_engineer.software_development.domain.booking.entity;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailableSeat {

    private String flightId;

    private Integer availableSeats;
}
