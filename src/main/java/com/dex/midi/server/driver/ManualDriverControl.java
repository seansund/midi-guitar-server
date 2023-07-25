package com.dex.midi.server.driver;

import com.dex.midi.DriverControl;

public interface ManualDriverControl extends DriverControl {
    void close();
}
