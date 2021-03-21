package ru.mephi.scaling.caclucation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.scaling.caclucation.service.CalculatingService;

@RestController
public class CalculationController {

    private final CalculatingService calculatingService;

    public CalculationController(CalculatingService calculatingService) {
        this.calculatingService = calculatingService;
    }

    @GetMapping(value = "/calculate")
    public Integer calculateNodeCount(@RequestParam Long latency) {
        return calculatingService.calculateNodeCount(latency);
    }

}
