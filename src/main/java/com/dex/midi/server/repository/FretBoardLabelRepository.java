package com.dex.midi.server.repository;

import com.dex.midi.event.MidiEventObservableSource;
import com.dex.midi.event.PitchEvent;
import com.dex.midi.model.GuitarPosition;
import com.dex.midi.server.model.*;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FretBoardLabelRepository {
    private final FretBoardConfigRepository configRepository;
    private final FretBoardLayoutRepository layoutRepository;
    private final KeyPressRepository keyPressRepository;
    private final BehaviorSubject<FretBoardLabels> subject;

    private final Disposable disposable;

    private KeyboardPage page = KeyboardPage.getDefault();
    private KeyboardShift shift = KeyboardShift.getDefault();

    public FretBoardLabelRepository(
            FretBoardConfigRepository configRepository,
            FretBoardLayoutRepository layoutRepository,
            KeyPressRepository keyPressRepository,
            MidiEventObservableSource observableSource
    ) {
        super();

        this.configRepository = configRepository;
        this.layoutRepository = layoutRepository;
        this.keyPressRepository = keyPressRepository;

        subject = BehaviorSubject.createDefault(new FretBoardLabels());

        final Disposable configDisp = configRepository
                .getObservable()
                .subscribe(this::handleFretBoardConfigChange);

        final Disposable noteOnDisp = observableSource
                .getNoteOnObservable()
                .subscribe(this::handleNoteOn);

        disposable = new CompositeDisposable(configDisp, noteOnDisp);
    }

    @PreDestroy
    public void close() {
        subject.onComplete();
        disposable.dispose();
    }

    public FretBoardLabels getFretBoardLabels() {
        return subject.getValue();
    }

    public Observable<List<FretBoardLabel>> fretBoardLabels() {
        return subject.map(FretBoardLabels::getLabels);
    }

    protected void handleNoteOn(PitchEvent e) {
        final GuitarPosition pos = e.getGuitarFret();

        final FretBoardConfig config = configRepository.getFretBoardConfig();

        if ("notes".equals(config.getMode())) {
            return;
        }

        final FretBoardLayout layout = layoutRepository.getFretBoardLayout(config.getMode());

        final Optional<FretBoardLabel> label = getFretBoardLabel(layout, pos);

        if (label.isPresent()) {
            if ("page".equalsIgnoreCase(label.get().getLabel())) {
                page = page.next();

                handleFretBoardChange(config, layout);
            } else if ("shift".equalsIgnoreCase(label.get().getLabel())) {
                shift = shift.next();

                handleFretBoardChange(config, layout);
            } else {
                fireKeyPressed(label.get());

                if (page.isDefault() && !shift.isDefault()) {
                    shift = shift.reset();

                    handleFretBoardChange(config, layout);
                }
            }
        }
    }

    protected Optional<FretBoardLabel> getFretBoardLabel(FretBoardLayout layout, GuitarPosition pos) {

        final FretBoardLabels labels = layout.getFretBoardLabels(page, shift);

        return labels.getLabels()
                .stream()
                .filter(FretBoardLabel.byGuitarPosition(pos))
                .findFirst();
    }

    protected void handleFretBoardConfigChange(FretBoardConfig config) {
        final FretBoardLayout layout = layoutRepository.getFretBoardLayout(config.getMode());

        page = page.reset();
        shift = shift.reset();

        handleFretBoardChange(config, layout);
    }

    protected void handleFretBoardChange(FretBoardConfig config, FretBoardLayout layout) {

        FretBoardLabels labels = layout
                .getFretBoardLabels(page, shift)
                .transposeKey(config.getKey());

        System.out.println(" *** FretBoardConfig change: " + config.getMode() + ", " + config.getKey());
        if (labels != null && !labels.equals(getFretBoardLabels())) {
            subject.onNext(labels);
        }
    }

    protected void fireKeyPressed(FretBoardLabel key) {
        keyPressRepository.fireKeyPressed(key.getLabel());
    }
}
