package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloWebFluxController {
    @GetMapping("/hellowebflux")
    public Mono<String> helloWebFlux() {
        return Mono.just("This is WebFlux demo");
    }
}
