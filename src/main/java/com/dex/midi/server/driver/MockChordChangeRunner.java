package com.dex.midi.server.driver;

import com.dex.midi.event.MidiEventObservableSource;
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

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component()
@Profile("mockChordChange")
public class MockChordChangeRunner implements ApplicationRunner {
    private final MidiEventObservableSource producer;
    private Disposable disposable;

    public MockChordChangeRunner(MidiEventObservableSource source) {
        this.producer = source;
    }

    @Override
    public void run(ApplicationArguments args) {

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

    @PreDestroy
    public void close() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
