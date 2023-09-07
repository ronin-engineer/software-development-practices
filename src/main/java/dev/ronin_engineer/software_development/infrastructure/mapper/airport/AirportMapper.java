package dev.ronin_engineer.software_development.infrastructure.mapper.airport;

import dev.ronin_engineer.software_development.domain.airport.entity.Airport;

import java.util.List;

public interface AirportMapper {

    List<Airport> findAll();
}
