package com.alewicki.counter.service;

import com.alewicki.counter.service.jump.JumpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class CounterConfiguration {



    @Bean
    public CounterService counterService(
            JumpService jumpService,
            @Value("${keys.enabled}")
            List<String> keyPresses
    ) {
        return new CounterService(
                new AtomicInteger(),
                Instant.now(),
                Instant.now(),
                new ArrayList<>(),
                jumpService,
                keyPresses,
                KeyPress.SPACE

        );
    }

}
