package ru.mephi.scaling.caclucation.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CalculatingServiceImpl implements CalculatingService {

    @Override
    public Integer calculateNodeCount(Long latency) {
        Random random = new Random();
        return random.nextInt(4) + 1;
    }
}
