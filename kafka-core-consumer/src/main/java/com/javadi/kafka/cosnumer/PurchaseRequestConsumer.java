package com.javadi.kafka.cosnumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.javadi.kafka.entity.PurchaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseRequestConsumer {

	private static final Logger LOG = LoggerFactory.getLogger(PurchaseRequestConsumer.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	@Qualifier("cachePurchaseRequest")
	private Cache<Integer, Boolean> cache;
	
	private boolean doesExistInCache(int purchaseRequestId) {
		return Optional.ofNullable(cache.getIfPresent(purchaseRequestId)).orElse(false);
	}
	
	@KafkaListener(topics = "t-purchase-request")
	public void consume(String message) throws JsonMappingException, JsonProcessingException {
		var purchaseRequest = objectMapper.readValue(message, PurchaseRequest.class);
		
		var processed = doesExistInCache(purchaseRequest.getId());
		
		if (processed) {
			LOG.info("the message is already processed ant won't be processed again: {}", purchaseRequest);
			return;
		}
		
		LOG.info("Processing {}", purchaseRequest);
		
		cache.put(purchaseRequest.getId(), true);
	}
	
}
