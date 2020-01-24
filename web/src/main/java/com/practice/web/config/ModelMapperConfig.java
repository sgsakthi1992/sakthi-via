package com.practice.web.config;

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
