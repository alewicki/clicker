package com.alewicki.counter.service.trigger.tipply;


import com.alewicki.counter.service.CounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class TipplyTrigger {
    private final CounterService counterService;
    private final RestTemplate restTemplate;
    private final String url;
    private List<TipplyRecord> topWeek;

    @Scheduled(fixedDelay = 5000)
    public void check() {

        List<TipplyRecord> topWeek = Arrays.stream(restTemplate
                        .getForObject(url, TipplyRecord[].class))
                .collect(Collectors.toList());
        if (this.topWeek == null) {
            this.topWeek = topWeek;
            log.info("Initializing list {}", topWeek);
            return;
        }

        findNewRecords(this.topWeek, topWeek)
                .forEach(this::handle);

        this.topWeek = topWeek;

    }

    private List<TipplyRecord> findNewRecords(List<TipplyRecord> oldList,
                                              List<TipplyRecord> newList) {

        int maxOverlap = Math.min(oldList.size(), newList.size());
        log.info("Comparing");
        log.info("New list {}", oldList);
        log.info("Old list {}", newList);

        for (int overlap = maxOverlap; overlap >= 1; overlap--) {

            boolean matches = true;

            for (int i = 0; i < overlap; i++) {
                if (!oldList.get(i).equals(newList.get(newList.size() - overlap + i))) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                return new ArrayList<>(newList.subList(0, newList.size() - overlap));
            }
        }

        return new ArrayList<>(newList);
    }

    private void handle(TipplyRecord tipplyRecord) {
        log.info("Donation detected: {}", tipplyRecord);
        int plnAmount = tipplyRecord.getAmount() / 100;
        counterService.add(plnAmount);
    }
}
