package com.dex.midi.server.driver;

import com.dex.midi.MidiDriver;
import com.dex.midi.event.MidiEventListenerSource;
import com.dex.midi.event.MidiEventObservableSource;

public class MockDriver implements MidiDriver {
    @Override
    public MidiEventListenerSource getMidiEventListenerSource() {
        return null;
    }

    @Override
    public MidiEventObservableSource getMidiEventObservableSource() {
        return null;
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void run() {

    }
}
