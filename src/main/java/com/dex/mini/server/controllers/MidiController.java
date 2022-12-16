package com.dex.mini.server.controllers;

import com.dex.midi.chord.Chord;
import com.dex.midi.event.PitchEvent;
import com.dex.mini.server.repository.DataRepository;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
public class MidiController {

    private final DataRepository repository;

    public MidiController(DataRepository dataRepository) {
        this.repository = dataRepository;
    }

    @QueryMapping
    public List<Chord> chord() {
        return this.repository.getChord();
    }

    @SubscriptionMapping
    public Flux<PitchEvent> noteOn() {
        return this.repository.getNoteOnStream();
    }

    @SubscriptionMapping
    public Flux<PitchEvent> noteOff() {
        return this.repository.getNoteOffStream();
    }

    @SubscriptionMapping
    public Flux<List<Chord>> chordChange() {
        return this.repository.getChordChangeStream();
    }

}
