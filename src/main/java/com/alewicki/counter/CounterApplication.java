package com.alewicki.counter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties
public class CounterApplication {

	public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CounterApplication.class);

        builder.headless(false);

        ConfigurableApplicationContext context = builder.run(args);
	}

}
