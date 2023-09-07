package dev.ronin_engineer.software_development.application.endpoint.rest;

import dev.ronin_engineer.software_development.application.aop.Authorize;
import dev.ronin_engineer.software_development.application.dto.response.ResponseDto;
import dev.ronin_engineer.software_development.domain.airport.entity.Airport;
import dev.ronin_engineer.software_development.domain.auth.constant.Action;
import dev.ronin_engineer.software_development.domain.auth.constant.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${application-name}/v1/airports")
public class AirportController {


    @PostMapping
    @Authorize(action = Action.CREATE, resource = Resource.AIRPORTS)
    public String createAirport() {
        return "OK";
    }
}
