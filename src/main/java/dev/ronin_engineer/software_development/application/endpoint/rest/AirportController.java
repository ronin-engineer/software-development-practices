package dev.ronin_engineer.software_development.application.endpoint.rest;


import dev.ronin_engineer.software_development.application.dto.response.ResponseDto;
import dev.ronin_engineer.software_development.application.service.ResponseFactory;
import dev.ronin_engineer.software_development.domain.airport.entity.Airport;
import dev.ronin_engineer.software_development.domain.airport.service.AirportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/${application-name}/v1/airports")
public class AirportController {

    private final AirportService airportService;

    private final ResponseFactory responseFactory;

    public AirportController(AirportService airportService, ResponseFactory responseFactory) {
        this.airportService = airportService;
        this.responseFactory = responseFactory;
    }
    

    @GetMapping
    public ResponseDto getAllAirports() {
        List<Airport> airports = airportService.getAll();
        return responseFactory.response(airports);
    }
}
