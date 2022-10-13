package de.telekom.bonicheckprototype.controller;

import co.elastic.apm.api.CaptureTransaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApmTestController {

    @CaptureTransaction
    @GetMapping("/super-fast")
    public String getSuperFastApi() {

        return "I'm super fast.";
    }

    @CaptureTransaction
    @GetMapping("/fast")
    public String getFastApi() throws InterruptedException {

        Thread.sleep(20); // sleep for 20 milliseconds
        return "I'm fast!";
    }

    @CaptureTransaction
    @GetMapping("/slow")
    public String getSlowApi() throws InterruptedException {

        Thread.sleep(3000); // sleep for 3 seconds
        return "I'm slow :(";
    }

    @CaptureTransaction
    @GetMapping("/super-slow")
    public String getSuperSlowApi() throws InterruptedException {

        Thread.sleep(10000); // sleep for 1 minute!
        return "I'm super slow. Refactor me before moving to production!! :)";
    }
}
