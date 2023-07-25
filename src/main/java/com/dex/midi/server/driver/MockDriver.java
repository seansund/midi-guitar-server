package com.dex.midi.server.driver;

import com.dex.midi.MidiDriver;
import com.dex.midi.event.MidiEventListenerSource;
import com.dex.midi.event.MidiEventObservableSource;
import com.dex.midi.event.PitchEvent;
import com.dex.midi.event.SimpleMidiEventProducer;
import com.dex.midi.model.GuitarPosition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

@Component("MidiDriver")
@Profile("mock")
public class MockDriver implements MidiDriver, ApplicationRunner {
    private final SimpleMidiEventProducer producer;

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
    public void close() {
        producer.close();
    }

    @Override
    public void run() {
        System.out.println(" **  Running MockDriver");
    }

    public static PitchEvent noteOnPitchEvent(final GuitarPosition pos) {
        try {
            final ShortMessage msg = new ShortMessage(144, pos.getStringIndex(), 0, 0);

            return new PitchEvent(msg, 1000, pos);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    public static PitchEvent noteOffPitchEvent(int stringIndex) {
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
