package com.dex.midi.server;

import com.dex.midi.MidiDriver;
import com.dex.midi.server.model.GuitarKey;
import com.dex.midi.server.repository.FretBoardConfigRepository;
import com.dex.midi.util.CircularIterator;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ServerApplication implements ApplicationRunner, AutoCloseable {

	private final MidiDriver driver;
	private final FretBoardConfigRepository repository;
	private Disposable disposable;

	public ServerApplication(MidiDriver driver, FretBoardConfigRepository repository) {
		this.driver = driver;
		this.repository = repository;
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("**** Starting up");

		final Iterator<GuitarKey> keys = new CircularIterator<GuitarKey>(repository.getAvailableKeys());

		disposable = Observable
				.interval(1, 2, TimeUnit.SECONDS)
				.subscribe((Long next) -> {
					repository.updateGuitarKey(keys.next());
				});
	}

	@Override
	public void close() throws Exception {
		if (disposable != null) {
			disposable.dispose();
		}
	}
}
