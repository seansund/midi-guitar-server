package com.dex.midi.server.driver;

import com.dex.midi.MidiDriver;
import com.dex.midi.event.MidiEventListenerSource;
import com.dex.midi.event.MidiEventObservableSource;
import com.dex.midi.event.PitchEvent;
import com.dex.midi.event.SimpleMidiEventProducer;
import com.dex.midi.model.GuitarPosition;
import com.dex.midi.model.GuitarPositions;
import com.dex.midi.server.model.GuitarPositionFactory;
import com.dex.midi.util.CircularIterator;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component("MidiDriver")
@Profile("mock")
public class MockDriver implements MidiDriver, ApplicationRunner {
    private final SimpleMidiEventProducer producer;
    private Disposable disposable;

    public MockDriver() {
        System.out.println("Creating MockDriver");
        producer = SimpleMidiEventProducer.getInstance();
    }

    @Override
    public MidiEventListenerSource getMidiEventListenerSource() {
        return producer;
    }

    @Override
    public MidiEventObservableSource getMidiEventObservableSource() {
        return producer;
    }

    @Override
    @PreDestroy
    public void close() throws Exception {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        producer.close();
    }

    @Override
    public void run() {
        System.out.println(" **  Running MockDriver");

        final Iterator<GuitarPositions> positionsIterator = new CircularIterator<>(Arrays.asList(
                GuitarPositionFactory.fromFrets(0, 1, 0, 2, 3),
                GuitarPositionFactory.fromFrets(),
                GuitarPositionFactory.fromFrets(3, 3, 0, 0, 2, 3),
                GuitarPositionFactory.fromFrets(),
                GuitarPositionFactory.fromFrets(2, 3, 2, 0),
                GuitarPositionFactory.fromFrets(),
                GuitarPositionFactory.fromFrets(0, 1, 2, 2, 0),
                GuitarPositionFactory.fromFrets(),
                GuitarPositionFactory.fromFrets(0, 0, 0, 2, 2, 0),
                GuitarPositionFactory.fromFrets()
        ));

        disposable = Observable
                .interval(5, TimeUnit.SECONDS)
                .subscribe(next -> {
                    final GuitarPosition[] positions = positionsIterator.next().getPositions();

                    for (int stringIndex = 0; stringIndex < positions.length; stringIndex++) {
                        final GuitarPosition pos = positions[stringIndex];

                        if (pos == null) {
                            producer.fireNoteOff(MockDriver.noteOffPitchEvent(stringIndex));
                        } else {
                            producer.fireNoteOn(MockDriver.noteOnPitchEvent(pos));
                        }
                    }
                });
    }

    private static PitchEvent noteOnPitchEvent(final GuitarPosition pos) {
        try {
            final ShortMessage msg = new ShortMessage(144, pos.getStringIndex(), 0, 0);

            return new PitchEvent(msg, 1000, pos);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    private static PitchEvent noteOffPitchEvent(int stringIndex) {
        try {
            final ShortMessage msg = new ShortMessage(128, stringIndex, 0, 0);

            return new PitchEvent(msg, 1000, new GuitarPosition(stringIndex, 0));
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        run();
    }
}
