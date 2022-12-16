package com.dex.mini.server.driver;

import com.dex.midi.MidiGuitarReceiver;
import com.dex.midi.chord.MidiChordSubscriber;
import com.dex.midi.chord.SimpleChordEventProducer;
import com.dex.midi.event.SimpleMidiControlEventProducer;
import com.dex.midi.event.SimpleMidiEventProducer;
import org.springframework.stereotype.Component;

import javax.sound.midi.Transmitter;

@Component
public class Driver {

    private Transmitter transmitter;
    private MidiGuitarReceiver receiver;
    private SimpleMidiEventProducer midiEventProducer;
    private SimpleChordEventProducer chordEventProducer;
    private SimpleMidiControlEventProducer midiControlEventProducer;

    public Driver(Transmitter transmitter, MidiGuitarReceiver receiver, SimpleMidiEventProducer midiEventProducer, SimpleChordEventProducer chordEventProducer, SimpleMidiControlEventProducer midiControlEventProducer) {
        this.transmitter = transmitter;
        this.receiver = receiver;
        this.midiEventProducer = midiEventProducer;
        this.chordEventProducer = chordEventProducer;
    }

    public void run() {
        MidiChordSubscriber sub = new MidiChordSubscriber(midiEventProducer, chordEventProducer);

        try {
            while (!Thread.interrupted()) {
                Thread.sleep(5000);
            }
        } catch (InterruptedException ex) {
            this.close();
        }
    }

    public void close() {
        if (midiEventProducer != null) {
            midiEventProducer.close();
        }

        if (chordEventProducer != null) {
            chordEventProducer.close();
        }

        if (transmitter != null) {
            transmitter.close();
            transmitter = null;
        }

        if (receiver != null) {
            receiver.close();
            receiver = null;
        }

    }
}
