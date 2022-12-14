package com.javadi.kafka.cosnumer;

import com.javadi.kafka.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

//@Service
public class EmployeeJsonConsumer {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeJsonConsumer.class);
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@KafkaListener(topics = "t-employee")
	public void listen(String message) throws JsonMappingException, JsonProcessingException {
		var employee = objectMapper.readValue(message, Employee.class);
		LOG.info("Employee is : {}", employee);
	}
	
}
