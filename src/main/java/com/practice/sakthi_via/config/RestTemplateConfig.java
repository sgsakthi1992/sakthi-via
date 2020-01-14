/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    /**
     * Rest Template bean configuration.
     *
     * @return ret template bean
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
