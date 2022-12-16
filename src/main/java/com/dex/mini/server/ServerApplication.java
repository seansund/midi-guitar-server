package com.dex.mini.server;

import com.dex.mini.server.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication implements ApplicationRunner {

	@Autowired
	private Driver driver;

	public static void main(String[] args) {

		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		driver.run();
	}

}
