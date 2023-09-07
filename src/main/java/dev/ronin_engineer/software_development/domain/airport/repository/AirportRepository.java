package dev.ronin_engineer.software_development.domain.airport.repository;

import dev.ronin_engineer.software_development.domain.airport.entity.Airport;

import java.util.List;

public interface AirportRepository {
    List<Airport> findAll();
}
