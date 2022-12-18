package com.dex.midi.server.controllers;

import com.dex.midi.server.model.*;
import com.dex.midi.server.repository.FretBoardConfigRepository;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
public class FretBoardConfigController {
    private final FretBoardConfigRepository repo;

    public FretBoardConfigController(FretBoardConfigRepository repo) {
        this.repo = repo;
    }

    @QueryMapping
    public List<GuitarKey> getAvailableKeys() {
        final List<GuitarKey> keys = repo.getAvailableKeys();
        System.out.println("Getting keys: " + keys);
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
    public FretBoardConfig updateFretBoardMode(String mode) {
        return repo.updateFretBoardMode(FretBoardModes.lookupMode(mode));
    }

    @MutationMapping
    public FretBoardConfig updateGuitarKey(String key) {
        return repo.updateGuitarKey(GuitarKeys.lookupKey(key));
    }

    @SubscriptionMapping
    public Flux<FretBoardConfig> fretBoardConfig() {
        return Flux.from(repo.getObservable().toFlowable(BackpressureStrategy.BUFFER));
    }
}
