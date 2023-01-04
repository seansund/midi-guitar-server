package com.dex.midi.server.repository;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

@Repository
public class KeyPressRepository {
    private final BehaviorSubject<String> subject;

    public KeyPressRepository() {
        subject = BehaviorSubject.createDefault("");
    }

    public void fireKeyPressed(String key) {
        subject.onNext(key);
    }

    public String getLastKeyPressed() {
        return subject.getValue();
    }

    public Observable<String> keysPressed() {
        return subject;
    }

    @PreDestroy
    public void close() {
        subject.onComplete();
    }
}
