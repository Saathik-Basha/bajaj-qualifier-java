package com.Bajaj.Bajaj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Startup implements CommandLineRunner {
    private final SolutionService solutionService;

    public Startup(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @Override
    public void run(String... args) throws Exception {
        solutionService.startProcess();
    }

}
