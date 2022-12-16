package com.dex.mini.server.config;

import com.dex.midi.MidiConfig;
import com.dex.midi.MidiGuitarReceiver;
import com.dex.midi.chord.SimpleChordEventProducer;
import com.dex.midi.event.SimpleMidiControlEventProducer;
import com.dex.midi.event.SimpleMidiEventProducer;
import com.dex.mini.server.driver.Driver;
import org.springframework.context.annotation.Bean;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

public class MidiSystem {

    @Bean
    public MidiGuitarReceiver getReceiver(MidiConfig config) {
        return new MidiGuitarReceiver(config);
    }

    @Bean
    public Transmitter getTransmitter(MidiGuitarReceiver receiver) throws MidiUnavailableException {
        Transmitter transmitter = javax.sound.midi.MidiSystem.getTransmitter();

        transmitter.setReceiver(receiver);

        return transmitter;
    }

    @Bean
    public SimpleMidiEventProducer getMidiEventProducer() {
        return new SimpleMidiEventProducer();
    }

    @Bean
    public SimpleChordEventProducer getChordEventProducer() {
        return new SimpleChordEventProducer();
    }

    @Bean
    public SimpleMidiControlEventProducer getMidiControlEventProducer() {
        return new SimpleMidiControlEventProducer();
    }

    @Bean
    public MidiConfig getMidiConfig() {
        return new MidiConfig();
    }

    @Bean
    public Driver getDriver(Transmitter transmitter, MidiGuitarReceiver receiver, SimpleMidiEventProducer midiEventProducer, SimpleChordEventProducer chordEventProducer, SimpleMidiControlEventProducer midiControlEventProducer) {
        return new Driver(transmitter, receiver, midiEventProducer, chordEventProducer, midiControlEventProducer);
    }
}
