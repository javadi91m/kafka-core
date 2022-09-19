package com.javadi.kafka.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.javadi.kafka.entity.PaymentRequestCacheKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

	// key is PurchaseRequest.id
	@Bean(name = "cachePurchaseRequest")
	public Cache<Integer, Boolean> cachePurchaseRequest() {
		return Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(2)).maximumSize(1000).build();
	}

	// key is a POJO (PaymentRequestCacheKey)
	@Bean(name = "cachePaymentRequest")
	public Cache<PaymentRequestCacheKey, Boolean> cachePaymentRequest() {
		return Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(2)).maximumSize(1000).build();
	}

}
