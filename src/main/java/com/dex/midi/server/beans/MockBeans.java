package com.dex.midi.server.beans;

import com.dex.midi.MidiDriver;
import com.dex.midi.server.driver.MockDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("mock")
public class MockBeans {
    @Bean
    public MidiDriver getDriver() {
        return new MockDriver();
    }
}