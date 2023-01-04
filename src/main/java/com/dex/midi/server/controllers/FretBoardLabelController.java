package com.dex.midi.server.controllers;

import com.dex.midi.server.model.FretBoardConfig;
import com.dex.midi.server.model.FretBoardLabel;
import com.dex.midi.server.repository.FretBoardLabelRepository;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
@CrossOrigin
public class FretBoardLabelController {
    private final FretBoardLabelRepository repo;

    public FretBoardLabelController(FretBoardLabelRepository repo) {
        this.repo = repo;
    }

    @QueryMapping
    public List<FretBoardLabel> getFretBoardLabels() {
        return repo.getFretBoardLabels().getLabels();
    }

    @SubscriptionMapping
    public Flux<List<FretBoardLabel>> fretBoardLabels() {
        System.out.println("**** Subscribe to fretBoardLabels");

        return RxJava3Adapter.observableToFlux(repo.fretBoardLabels(), BackpressureStrategy.LATEST);
    }
}
