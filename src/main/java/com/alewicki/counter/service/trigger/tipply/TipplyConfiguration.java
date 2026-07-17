package com.alewicki.counter.service.trigger.tipply;

import com.alewicki.counter.service.CounterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnBooleanProperty("tipply.enabled")
public class TipplyConfiguration {

    @Bean
    public TipplyTrigger trigger(
            final CounterService counterService,
            @Value("${tipply.url}") final String url
    ){
        return new TipplyTrigger(counterService,new RestTemplate(),url);
    }
}
