package com.dex.midi.server.driver;

import com.dex.midi.MidiDriver;
import com.dex.midi.event.MidiEventListenerSource;
import com.dex.midi.event.MidiEventObservableSource;
import com.dex.midi.event.PitchEvent;
import com.dex.midi.event.SimpleMidiEventProducer;
import com.dex.midi.model.GuitarPosition;
import com.dex.midi.model.GuitarPositions;
import com.dex.midi.server.model.GuitarKey;
import com.dex.midi.server.model.GuitarPositionFactory;
import com.dex.midi.server.repository.FretBoardConfigRepository;
import com.dex.midi.util.CircularIterator;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class MockDriver implements MidiDriver {
    private final SimpleMidiEventProducer producer;
    private final FretBoardConfigRepository repository;
    private Disposable disposable;

    public MockDriver(FretBoardConfigRepository repository) {
        System.out.println("Creating MockDriver");
        producer = SimpleMidiEventProducer.getInstance();
        this.repository = repository;
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
    public void close() throws Exception {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        producer.close();
    }

    @Override
    public void run() {
        System.out.println(" **  Running MockDriver");

        runGuitarKeyEvents();
        runGuitarEvents();
    }

    public void runGuitarEvents() {
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

        setDisposable(
                Observable
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
                        })
        );
    }

    public void runGuitarKeyEvents() {
        final Iterator<GuitarKey> keys = new CircularIterator<>(repository.getAvailableKeys());

        setDisposable(
                Observable
                        .interval(1, 2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(next -> repository.updateGuitarKey(keys.next()))
        );
    }

    private void setDisposable(Disposable disp) {
        if (this.disposable == null) {
            this.disposable = disp;
        } else {
            this.disposable = new CompositeDisposable(this.disposable, disp);
        }
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
}
