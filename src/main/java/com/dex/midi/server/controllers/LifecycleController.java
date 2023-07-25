package com.dex.midi.server.controllers;

import com.dex.midi.server.driver.ManualDriverControl;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@CrossOrigin
@RestController
public class LifecycleController {
    private final ManualDriverControl control;

    public LifecycleController(ManualDriverControl control) {
        this.control = control;
    }

    @GetMapping("/api/stop")
    public String stopRest() {
        control.close();

        return "Stopped";
    }

    @MutationMapping
    public String stop() {
        control.close();

        return "Stopped";
    }
}
