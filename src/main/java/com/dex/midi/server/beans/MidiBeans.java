package com.dex.midi.server.beans;

import com.dex.midi.Driver;
import com.dex.midi.MidiDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("default")

public class MidiBeans {
    @Bean
    public MidiDriver getDriver() {
        return new Driver();
    }
}
