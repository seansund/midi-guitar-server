package com.dex.midi.server.repository;

import com.dex.midi.server.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

@Repository
public class FretBoardLayoutRepository {
    private Map<String, FretBoardLayout> layouts;
    private String defaultMode;

    private final ResourceLoader resourceLoader;

    public FretBoardLayoutRepository(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

        layouts = new HashMap<>();
    }

    private static LoaderOptions buildLoaderOptions() {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false);
        return loaderOptions;
    }

    @PostConstruct
    public void init() {
        final String location = "classpath:/fret-board-config.yml";
        try {
            final Resource configResource = resourceLoader.getResource(location);

            Yaml yaml = new Yaml(new Constructor(FretBoardLayoutConfig.class, buildLoaderOptions()));

            final FretBoardLayoutConfig layoutConfig = yaml.load(configResource.getInputStream());

            defaultMode = layoutConfig.getDefaultMode();

            layouts = layoutConfig.getModes()
                    .stream()
                    .map(this::initializeLayout)
                    .collect(Collectors.toMap(FretBoardLayout::getMode, identity()));
        } catch (IOException ex) {
            throw new RuntimeException("Error getting stream from " + location, ex);
        }
    }

    private FretBoardLayout initializeLayout(FretBoardLayout layout) {
        final List<FretBoardLayoutPage> pages = layout.getPages()
                .stream()
                .map(this.initializeLayoutPages(layout))
                .toList();

        return layout.withPages(pages);
    }

    private Function<FretBoardLayoutPage, FretBoardLayoutPage> initializeLayoutPages(FretBoardLayout layout) {
        return (FretBoardLayoutPage page) -> this.initializeLayoutPage(page, layout.getKey());
    }

    private FretBoardLayoutPage initializeLayoutPage(FretBoardLayoutPage page, GuitarKey baseKey) {
        final String location = "classpath:" + page.getFile();

        try {
            final Resource resource = resourceLoader.getResource(location);

            List<List<String>> baseFrets = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    baseFrets.add(Arrays.stream(values).map(String::trim).toList());
                }
            }

            List<List<String>> frets = new ArrayList<>();
            for (int i = 0; i < FretBoardConstants.FRET_COUNT; i++) {
                final int index = i < baseFrets.size() ? i : i % 12;

                frets.add(baseFrets.get(index));
            }

            return page.withLabels(FretBoardLabels.fromFrets(baseKey, frets));
        } catch (IOException ex) {
            throw new RuntimeException("Error reading from input stream: " + location, ex);
        }
    }

    public FretBoardMode getDefaultMode() {
        return layouts.get(defaultMode);
    }

    public List<FretBoardMode> getAvailableModes() {
        return getModeStream().toList();
    }

    public FretBoardMode lookupMode(String mode) {
        return getModeStream(mode)
                .findFirst()
                .orElseThrow(() -> new FretBoardModeNotFound(mode));
    }

    protected Stream<FretBoardMode> getModeStream() {
        return getModeStream(null);
    }

    protected Stream<FretBoardMode> getModeStream(String modeFilter) {
        return layouts.entrySet()
                .stream()
                .filter(value -> modeFilter == null || modeFilter.equals(value.getKey()))
                .map(Map.Entry::getValue);
    }

    public FretBoardLayout getFretBoardLayout(String mode) {
        return Optional
                .ofNullable(layouts.get(mode))
                .orElseThrow(() -> new FretBoardModeNotFound(mode));
    }
}
