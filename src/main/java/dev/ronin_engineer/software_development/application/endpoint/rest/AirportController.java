package dev.ronin_engineer.software_development.application.endpoint.rest;

import dev.ronin_engineer.software_development.application.aop.Authorize;
import dev.ronin_engineer.software_development.application.dto.response.ResponseDto;
import dev.ronin_engineer.software_development.domain.airport.entity.Airport;
import dev.ronin_engineer.software_development.domain.auth.constant.Action;
import dev.ronin_engineer.software_development.domain.auth.constant.Resource;
import dev.ronin_engineer.software_development.infrastructure.service.LockService;
import dev.ronin_engineer.software_development.infrastructure.service.impl.DistributedLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/${application-name}/v1/airports")
public class AirportController {

    @Autowired
    DistributedLockService distributedLockService;


    @PostMapping
    @Authorize(action = Action.CREATE, resource = Resource.AIRPORTS)
    public String createAirport() {
        String requestId = "xxx";

        if (!distributedLockService.lock(requestId)) {
            return "reject";
        }

        String process = "OK";

        distributedLockService.unlock(requestId);
        return process;
    }

    // A -> B
    // B -> C

    public Long fundTransfer(String debitAccount, String creditAccount, long amount) {
        List<String> lockKeys = new LinkedList<>();
        lockKeys.add(debitAccount);
        lockKeys.add(creditAccount);

        if(!distributedLockService.lockWithRetry(lockKeys)) {
            return -1L;
        }

        Long process = amount;

        distributedLockService.unlock(lockKeys);
        return process;
    }
}
