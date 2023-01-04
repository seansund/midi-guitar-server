package com.dex.midi.server.controllers;

import com.dex.midi.server.model.*;
import com.dex.midi.server.repository.FretBoardConfigRepository;
import com.dex.midi.server.repository.FretBoardLayoutRepository;
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
    private final FretBoardConfigRepository configRepository;
    private final FretBoardLayoutRepository layoutRepository;

    public FretBoardConfigController(FretBoardConfigRepository configRepository, FretBoardLayoutRepository layoutRepository) {
        this.configRepository = configRepository;
        this.layoutRepository = layoutRepository;
    }

    @QueryMapping
    public List<GuitarKey> getAvailableKeys() {
        return configRepository.getAvailableKeys();
    }

    @QueryMapping
    public List<FretBoardMode> getAvailableModes() {
        return layoutRepository.getAvailableModes();
    }

    @QueryMapping
    public FretBoardConfig getFretBoardConfig() {
        return configRepository.getFretBoardConfig();
    }

    @MutationMapping
    public FretBoardConfig updateFretBoardMode(@Argument("mode") String mode) {
        return configRepository.updateFretBoardMode(layoutRepository.lookupMode(mode));
    }

    @MutationMapping
    public FretBoardConfig updateGuitarKey(@Argument("key") String key) {
        return configRepository.updateGuitarKey(GuitarKey.lookup(key));
    }

    @SubscriptionMapping
    public Flux<FretBoardConfig> fretBoardConfig() {
        System.out.println("**** Subscribe to fretBoardConfig");

        return RxJava3Adapter.observableToFlux(configRepository.getObservable(), BackpressureStrategy.LATEST);
    }
}
