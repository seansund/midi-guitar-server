package com.dex.midi.server.beans;

import com.dex.midi.MidiDriver;
import com.dex.midi.event.MidiEventObservableSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SupportBeans {
    @Bean
    public MidiEventObservableSource getMidiObservableObserverSource(MidiDriver driver) {
        return driver.getMidiEventObservableSource();
    }
}
