package com.dex.midi.server.controllers;

import com.dex.midi.model.GuitarPosition;
import com.dex.midi.server.model.ActiveGuitarPosition;
import com.dex.midi.server.model.ChordLabel;
import com.dex.midi.server.repository.GuitarEventRepository;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Controller
@CrossOrigin
public class GuitarEventController {
    private final GuitarEventRepository repo;

    public GuitarEventController(GuitarEventRepository repo) {
        this.repo = repo;
    }

    @QueryMapping
    public ActiveGuitarPosition[] getGuitarPositions() {
        return repo.getGuitarPositions();
    }

    @QueryMapping
    public Collection<ChordLabel> getChords() {
        return repo.getChords();
    }

    @MutationMapping
    public ActiveGuitarPosition[] pressNote(@Argument("stringIndex") int stringIndex, @Argument("fretIndex") int fretIndex) {

        final GuitarPosition position = new GuitarPosition(stringIndex, fretIndex);

        repo.pressNote(position);

        return repo.getGuitarPositions();
    }

    @SubscriptionMapping
    public Flux<ActiveGuitarPosition[]> guitarPositions() {
        return RxJava3Adapter.observableToFlux(repo.guitarPositions(), BackpressureStrategy.LATEST);
    }

    @SubscriptionMapping
    public Flux<Collection<ChordLabel>> chords() {
        return RxJava3Adapter.observableToFlux(repo.chords(), BackpressureStrategy.LATEST);
    }
}
