package com.dex.midi.server.driver;

import com.dex.midi.server.model.GuitarKey;
import com.dex.midi.server.repository.FretBoardConfigRepository;
import com.dex.midi.util.CircularIterator;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component()
@Profile("mockKeyChange")
public class MockKeyChangeRunner implements ApplicationRunner {
    private final FretBoardConfigRepository repository;

    private Disposable disposable;

    public MockKeyChangeRunner(FretBoardConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        final Iterator<GuitarKey> keys = new CircularIterator<>(repository.getAvailableKeys());

        disposable = Observable
                .interval(1, 10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .subscribe(next -> repository.updateGuitarKey(keys.next()));
    }

    @PreDestroy
    public void close() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            this.disposable = null;
        }
    }
}
