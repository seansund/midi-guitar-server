package com.dex.midi.server.controllers;

import com.dex.midi.server.repository.KeyPressRepository;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

@Controller
@CrossOrigin
public class KeyPressedController {
    private final KeyPressRepository repo;

    public KeyPressedController(KeyPressRepository repo) {
        this.repo = repo;
    }

    @QueryMapping
    public String getLastKeyPressed() {
        return repo.getLastKeyPressed();
    }

    @SubscriptionMapping
    public Flux<String> keyPressed() {
        return RxJava3Adapter.observableToFlux(repo.keysPressed(), BackpressureStrategy.LATEST);
    }
}
