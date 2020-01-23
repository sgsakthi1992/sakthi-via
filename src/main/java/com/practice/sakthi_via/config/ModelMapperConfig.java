/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    /**
     * ModelMapper bean.
     *
     * @return ModelMapper object
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
