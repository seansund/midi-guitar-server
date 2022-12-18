package com.dex.midi.server.controllers;

import com.dex.midi.server.model.FretBoardConfig;
import com.dex.midi.server.model.FretBoardModes;
import com.dex.midi.server.model.GuitarKey;
import com.dex.midi.server.model.GuitarKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.WebSocketGraphQlTester;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FretBoardConfigControllerITest {

    @LocalServerPort
    private int port;

    @Value("http://localhost:${local.server.port}${spring.graphql.websocket.path}")
    private String baseUrl;

    private GraphQlTester graphQlTester;

    @BeforeEach
    void setUp() {
        URI url = URI.create(baseUrl);
        this.graphQlTester = WebSocketGraphQlTester.builder(url, new ReactorNettyWebSocketClient()).build();
    }

    @Test
    void getAvailableKeys() {
        final GraphQlTester.EntityList<GuitarKey> value = this.graphQlTester.document("{ getAvailableKeys }")
                .execute()
                .path("getAvailableKeys")
                .entityList(GuitarKey.class);

        final List<GuitarKey> list = value.get();
        System.out.println("Guitar key result: " + list);

        value.contains(GuitarKeys.lookupKey("G"));
    }

    @Test
    void subscribeToFretBoardConfig() {
        Flux<FretBoardConfig> result = this.graphQlTester.document("subscription { fretBoardConfig }")
                .executeSubscription()
                .toFlux("fretBoardConfig", FretBoardConfig.class);

        StepVerifier.create(result)
                .expectNext(FretBoardConfig.of(GuitarKeys.defaultKey().getKey(), FretBoardModes.defaultMode().getMode()));
    }
}
