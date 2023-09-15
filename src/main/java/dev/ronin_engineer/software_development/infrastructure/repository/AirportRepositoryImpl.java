package dev.ronin_engineer.software_development.infrastructure.repository;

import dev.ronin_engineer.software_development.domain.airport.entity.Airport;
import dev.ronin_engineer.software_development.domain.airport.repository.AirportRepository;
import dev.ronin_engineer.software_development.infrastructure.mapper.airport.AirportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AirportRepositoryImpl implements AirportRepository {

    @Autowired
    private AirportMapper airportMapper;

    @Override
    @Cacheable(key = "#root.methodName", cacheNames = "localCache", cacheManager = "localCacheManager")
    public List<Airport> findAll() {
        return airportMapper.findAll();
    }
}
