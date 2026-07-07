package com.alewicki.counter.service;

import com.alewicki.counter.service.jump.HidJumpService;
import com.alewicki.counter.service.jump.JumpService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class CounterService {
    private AtomicInteger counter;
    private Instant previousDecrease;
    private Instant nextDecrease;
    private List<SseEmitter> emitters;
    private JumpService jumpService;
    private List<String> enabledKeys;
    private KeyPress keyPress;

    @Scheduled(fixedDelay = 150)
    public void tick() {

        if (counter.get() == 0)
            return;

        if (Instant.now().isAfter(nextDecrease)) {
            decrease();
        }
    }

    public SseEmitter subscribe() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        send(emitter);

        return emitter;
    }

    public void add(int amount) {
        counter.addAndGet(amount);
        broadcast();
    }

    public void decrease() {

        if (counter.get() == 0)
            return;

        counter.decrementAndGet();
        jumpService.jump(keyPress);
        Random random = new Random();
        keyPress = KeyPress.valueOf(enabledKeys.get(random.nextInt(enabledKeys.size())));
        previousDecrease = Instant.now();
        nextDecrease = previousDecrease.plusMillis(randomDelay());

        broadcast();
    }

    private long randomDelay() {
        return ThreadLocalRandom.current().nextLong(10, 15000);
    }

    private void broadcast() {

        Iterator<SseEmitter> iterator = emitters.iterator();

        while (iterator.hasNext()) {

            SseEmitter emitter = iterator.next();

            try {
                send(emitter);
            } catch (Exception e) {
                emitter.complete();
                iterator.remove();
            }
        }
    }

    @SneakyThrows
    private void send(SseEmitter emitter) {

        emitter.send(
                new CounterEvent(
                        counter.get(),
                        previousDecrease,
                        nextDecrease,
                        keyPress.name()
                )
        );
    }
}
