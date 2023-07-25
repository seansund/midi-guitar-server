package com.dex.midi.server.driver;

import org.springframework.stereotype.Component;

@Component("DriverControl")
public class DriverControlImpl extends Thread implements ManualDriverControl {

    private boolean running = true;
    private Object sync = new Object();

    @Override
    public void run() {
        waitInternal(false);
    }

    @Override
    public synchronized void close() {
        this.running = false;

        this.notifyAll();
    }

    protected synchronized void waitInternal(boolean throwEx) {
        while (running) {
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                running = false;

                if (throwEx) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void waitForClose() throws RuntimeException {
        waitInternal(true);
    }
}
