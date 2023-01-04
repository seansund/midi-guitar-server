package com.dex.midi.server.driver;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("MidiDriver")
@Profile("default")
public class MidiDriver extends com.dex.midi.Driver implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        run();
    }

    @PreDestroy
    public void close() {
        super.close();
    }
}
