package com.alewicki.counter.service;

import com.alewicki.counter.service.jump.JumpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
public class CounterConfiguration {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler =
                new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("scheduler-");
        scheduler.initialize();

        return scheduler;
    }

    @Bean
    public CounterService counterService(
            JumpService jumpService,
            @Value("${keys.enabled}")
            List<String> keyPresses,
            @Value("${keys.cost}")
            Integer cost,
            @Value("${keys.click.interval.min}")
            Integer min,
            @Value("${keys.click.interval.delta}")
            Integer delta
    ) {
        return new CounterService(
                new AtomicInteger(),
                Instant.now(),
                Instant.now(),
                new CopyOnWriteArrayList<>(),
                jumpService,
                keyPresses,
                KeyPress.SPACE,
                cost,
                min,
                delta

        );
    }

}
