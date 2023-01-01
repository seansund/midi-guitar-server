package com.dex.midi.server.controllers;

import com.dex.midi.server.model.*;
import com.dex.midi.server.repository.FretBoardConfigRepository;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
@CrossOrigin
public class FretBoardConfigController {
    private final FretBoardConfigRepository repo;

    public FretBoardConfigController(FretBoardConfigRepository repo) {
        this.repo = repo;
    }

    @QueryMapping
    public List<GuitarKey> getAvailableKeys() {
        final List<GuitarKey> keys = repo.getAvailableKeys();
        return keys;
    }

    @QueryMapping
    public List<FretBoardMode> getAvailableModes() {
        return repo.getAvailableModes();
    }

    @QueryMapping
    public FretBoardConfig getFretBoardConfig() {
        return repo.getFretBoardConfig();
    }

    @MutationMapping
    public FretBoardConfig updateFretBoardMode(@Argument("mode") String mode) {
        return repo.updateFretBoardMode(FretBoardModes.lookupMode(mode));
    }

    @MutationMapping
    public FretBoardConfig updateGuitarKey(@Argument("key") String key) {
        return repo.updateGuitarKey(GuitarKeys.lookupKey(key));
    }

    @SubscriptionMapping
    public Flux<FretBoardConfig> fretBoardConfig() {
        System.out.println("**** Subscribe to fretBoardConfig");

        return RxJava3Adapter.observableToFlux(repo.getObservable(), BackpressureStrategy.LATEST);
    }
}
