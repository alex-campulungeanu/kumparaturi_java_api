package com.alex.kumparaturi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class KumparaturiApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(KumparaturiApplication.class);

	@Autowired
	private Environment environment;

	@Override
    public void run(String... args) throws Exception {
		if(environment.getActiveProfiles().length !=1) {
			throw new RuntimeException("Application must have only one profile defined");
		}
		System.out.println("Current Directory = " + System.getProperty("user.dir"));
	    logger.info("App name: " + environment.getProperty("spring.application.name"));
	    logger.info("Active profiles: " + Arrays.toString(environment.getActiveProfiles()));
    }

	public static void main(String[] args) {
		SpringApplication.run(KumparaturiApplication.class, args);
	}

}
