package com.dex.midi.server.repository;

import com.dex.midi.server.model.*;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FretBoardConfigRepository {
    private final BehaviorSubject<FretBoardConfig> subject;

    public FretBoardConfigRepository() {
        System.out.println("**** New FretBoardConfigRepository");
        subject = BehaviorSubject.createDefault(
                FretBoardConfig.of(
                        GuitarKeys.instance().getDefaultKey().getKey(),
                        FretBoardModes.instance().getDefaultMode().getMode()
                )
        );
    }

    public List<GuitarKey> getAvailableKeys() {
        return GuitarKeys.instance().getKeys();
    }

    public List<FretBoardMode> getAvailableModes() {
        return FretBoardModes.instance().getModes();
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
}
