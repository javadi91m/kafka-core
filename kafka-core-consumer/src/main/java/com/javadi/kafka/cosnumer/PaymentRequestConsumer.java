package com.javadi.kafka.cosnumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.javadi.kafka.entity.PaymentRequest;
import com.javadi.kafka.entity.PaymentRequestCacheKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@Service
public class PaymentRequestConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentRequestConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("cachePaymentRequest")
    private Cache<PaymentRequestCacheKey, Boolean> cache;

    private boolean doesExistInCache(PaymentRequestCacheKey key) {
        return Optional.ofNullable(cache.getIfPresent(key)).orElse(false);
    }

    @KafkaListener(topics = "t-payment-request")
    public void consume(String message) throws JsonMappingException, JsonProcessingException {
        PaymentRequest paymentRequest = objectMapper.readValue(message, PaymentRequest.class);

        PaymentRequestCacheKey cacheKey = new PaymentRequestCacheKey(paymentRequest.getPaymentNumber(), paymentRequest.getAmount(), paymentRequest.getTransactionType());

        if (doesExistInCache(cacheKey)) {
            return;
        }

        LOG.info("Processing {}", paymentRequest);

        cache.put(cacheKey, true);
    }

}
