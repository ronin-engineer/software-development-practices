package dev.ronin_engineer.software_development.api.rest;

import dev.ronin_engineer.software_development.application.aop.Authorize;
import dev.ronin_engineer.software_development.domain.auth.constant.Action;
import dev.ronin_engineer.software_development.domain.auth.constant.Resource;
import dev.ronin_engineer.software_development.infrastructure.service.impl.DistributedLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/${application-name}/v1/airports")
public class AirportController {

    @Autowired
    DistributedLockService distributedLockService;


    @PostMapping
    @Authorize(action = Action.CREATE, resource = Resource.AIRPORTS)
    public String createAirport() {
        return "airport created";
    }
}
