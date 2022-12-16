package com.dex.mini.server.repository;

import com.dex.midi.chord.Chord;
import com.dex.midi.chord.SimpleChordEventProducer;
import com.dex.midi.event.PitchEvent;
import com.dex.midi.event.SimpleMidiControlEventProducer;
import com.dex.midi.event.SimpleMidiEventProducer;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.stereotype.Repository;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DataRepository {

    private SimpleMidiEventProducer midiEventProducer;
    private SimpleChordEventProducer chordEventProducer;
    private SimpleMidiControlEventProducer midiControlEventProducer;

    public DataRepository(SimpleMidiEventProducer midiEventProducer, SimpleChordEventProducer chordEventProducer, SimpleMidiControlEventProducer midiControlEventProducer) {
        this.midiEventProducer = midiEventProducer;
        this.chordEventProducer = chordEventProducer;
        this.midiControlEventProducer = midiControlEventProducer;
    }

    public List<Chord> getChord() {
        return chordEventProducer.getObservable().first(new ArrayList<Chord>()).blockingGet();
    }

    public Flux<PitchEvent> getNoteOnStream() {
        return RxJava3Adapter.observableToFlux(midiEventProducer.getNoteOnObservable(), BackpressureStrategy.BUFFER);
    }

    public Flux<PitchEvent> getNoteOffStream() {
        return RxJava3Adapter.observableToFlux(midiEventProducer.getNoteOffObservable(), BackpressureStrategy.BUFFER);
    }

    public Flux<List<Chord>> getChordChangeStream() {
        return RxJava3Adapter.observableToFlux(chordEventProducer.getObservable(), BackpressureStrategy.BUFFER);
    }
}
