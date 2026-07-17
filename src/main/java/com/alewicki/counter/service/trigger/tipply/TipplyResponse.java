package com.alewicki.counter.service.trigger.tipply;

import lombok.Data;

import java.util.List;

@Data
public class TipplyResponse {
    private List<TipplyRecord> top_week;
}
