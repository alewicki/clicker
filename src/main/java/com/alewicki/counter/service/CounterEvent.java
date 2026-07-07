package com.alewicki.counter.service;

import java.time.Instant;

public record CounterEvent (
        int counter,
        Instant previousDecrease,
        Instant nextDecrease,
        String keyToPress
){
}
