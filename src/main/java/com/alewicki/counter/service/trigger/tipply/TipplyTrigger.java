package com.alewicki.counter.service.trigger.tipply;


import com.alewicki.counter.service.CounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class TipplyTrigger {
    private final CounterService counterService;
    private final RestTemplate restTemplate;
    private final String url;
    private List<TipplyRecord> topWeek;

    @Scheduled(fixedDelay = 5000)
    public void check() {
        var topWeek = restTemplate
                .getForEntity(url, TipplyResponse.class)
                .getBody()
                .getTop_week();
        if (this.topWeek == null) {
            log.info("Initializing top week {}", topWeek);
            this.topWeek = topWeek;
        } else {
            if (this.topWeek.size() == topWeek.size()) {
                log.info("No tipply donate detected");
            } else {
                this.topWeek.forEach(topWeek::remove);
                topWeek.forEach(tipplyRecord -> {
                    log.info("Donation detected: {}", tipplyRecord);
                    this.topWeek.add(tipplyRecord);
                    int plnAmount = tipplyRecord.getAmount() / 100;
                    counterService.add(plnAmount);
                });
            }
        }


    }


}
