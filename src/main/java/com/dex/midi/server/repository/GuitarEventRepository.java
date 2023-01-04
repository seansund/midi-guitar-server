package com.dex.midi.server.repository;

import com.dex.midi.chord.MidiEventChordAdapter;
import com.dex.midi.event.MidiEventObservableSource;
import com.dex.midi.model.GuitarPositions;
import com.dex.midi.server.model.ActiveGuitarPosition;
import com.dex.midi.server.model.ChordLabel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

@Repository
public class GuitarEventRepository {
    private final BehaviorSubject<ActiveGuitarPosition[]> positions;
    private final BehaviorSubject<Collection<ChordLabel>> chords;

    public GuitarEventRepository(MidiEventObservableSource midiEventSource) {
        System.out.println("New GuitarEventRepository");

        positions = BehaviorSubject.createDefault(ActiveGuitarPosition.fromArray(new GuitarPositions().getPositions()));
        chords = BehaviorSubject.createDefault(new ArrayList<>());

        final MidiEventChordAdapter chordAdapter = new MidiEventChordAdapter()
                .registerObservable(midiEventSource);

        midiEventSource.getGuitarPositionObservable()
                .map(GuitarPositions::getPositions)
                .map(ActiveGuitarPosition::fromArray)
                .subscribe(this.positions);
        chordAdapter.getObservable()
                .map(ChordLabel::from)
                .subscribe(this.chords);
    }

    public ActiveGuitarPosition[] getGuitarPositions() {
        return positions.getValue();
    }

    public Collection<ChordLabel> getChords() {
        return chords.getValue();
    }

    public Observable<ActiveGuitarPosition[]> guitarPositions() {
        return this.positions;
    }

    public Observable<Collection<ChordLabel>> chords() {
        return this.chords;
    }

    @PreDestroy
    public void close() {
        positions.onComplete();
        chords.onComplete();
    }
}
