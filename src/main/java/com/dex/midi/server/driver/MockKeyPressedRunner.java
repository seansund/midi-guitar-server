package com.dex.midi.server.driver;

import com.dex.midi.event.MidiEventObservableSource;
import com.dex.midi.model.GuitarPosition;
import com.dex.midi.util.CircularIterator;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component()
@Profile("mockKeyPressed")
public class MockKeyPressedRunner implements ApplicationRunner {
    private final MidiEventObservableSource producer;
    private Disposable disposable;

    public MockKeyPressedRunner(MidiEventObservableSource source) {
        this.producer = source;
    }

    @Override
    public void run(ApplicationArguments args) {

        final Iterator<GuitarPosition> positionIterator = new CircularIterator<>(List.of());

        disposable = Observable
                .interval(5, TimeUnit.SECONDS)
                .subscribe(next -> {
                    final GuitarPosition position = positionIterator.next();

                    producer.fireNoteOn(MockDriver.noteOnPitchEvent(position));

                    Thread.sleep(500);

                    producer.fireNoteOff(MockDriver.noteOffPitchEvent(position.getStringIndex()));
                });
    }

    @PreDestroy
    public void close() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
