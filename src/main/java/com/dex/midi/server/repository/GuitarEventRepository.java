package com.dex.midi.server.repository;

import com.dex.midi.chord.MidiEventChordAdapter;
import com.dex.midi.event.MidiEventObservableSource;
import com.dex.midi.event.PitchEvent;
import com.dex.midi.model.GuitarPosition;
import com.dex.midi.model.GuitarPositions;
import com.dex.midi.server.model.ActiveGuitarPosition;
import com.dex.midi.server.model.ChordLabel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Repository
public class GuitarEventRepository {
    private final BehaviorSubject<ActiveGuitarPosition[]> positions;
    private final BehaviorSubject<Collection<ChordLabel>> chords;
    private final MidiEventObservableSource midiEventSource;

    public GuitarEventRepository(MidiEventObservableSource midiEventSource) {
        System.out.println("New GuitarEventRepository");

        this.midiEventSource = midiEventSource;
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

    public void pressNote(GuitarPosition position) {

        midiEventSource.fireNoteOn(new PitchEvent(noteOnMessage(), 1, position));

        final Thread t = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Sleep interrupted");
            }

            midiEventSource.fireNoteOff(new PitchEvent(noteOffMessage(), 1, position));
        });
        t.start();
    }

    @PreDestroy
    public void close() {
        positions.onComplete();
        chords.onComplete();
    }

    private static ShortMessage noteOnMessage() {
        return new ShortMessage();
    }

    private static ShortMessage noteOffMessage() {
        return new ShortMessage();
    }
}
