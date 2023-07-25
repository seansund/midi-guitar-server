package com.dex.midi.server.repository;

import com.dex.midi.server.model.FretBoardConfig;
import com.dex.midi.server.model.FretBoardMode;
import com.dex.midi.server.model.GuitarKey;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import jakarta.annotation.PreDestroy;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FretBoardConfigRepository {
    private final BehaviorSubject<FretBoardConfig> subject;

    public FretBoardConfigRepository(FretBoardLayoutRepository layoutRepository) {
        System.out.println("**** New FretBoardConfigRepository");
        subject = BehaviorSubject.createDefault(
                FretBoardConfig.of(
                        GuitarKey.defaultKey().getKey(),
                        layoutRepository.getDefaultMode().getMode()
                )
        );
    }

    public List<GuitarKey> getAvailableKeys() {
        return GuitarKey.getKeys();
    }

    public Observable<FretBoardConfig> getObservable() {
        return subject;
    }

    public FretBoardConfig getFretBoardConfig() {
        return subject.getValue();
    }

    public FretBoardConfig updateFretBoardMode(@NonNull FretBoardMode mode) {
        final FretBoardConfig newConfig = FretBoardConfig.of(getFretBoardConfig().getKey(), mode.getMode());

        subject.onNext(newConfig);

        return newConfig;
    }

    public FretBoardConfig updateGuitarKey(@NonNull GuitarKey key) {
        final FretBoardConfig newConfig = FretBoardConfig.of(key.getKey(), getFretBoardConfig().getMode());

        subject.onNext(newConfig);

        return newConfig;
    }

    @PreDestroy
    public void close() {
        subject.onComplete();
    }
}
