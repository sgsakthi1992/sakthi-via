/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.config;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCircuitBreaker
@EnableHystrixDashboard
public class CircuitBreakerConfig {
}
