package com.practice.sakthi_via.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TodoFacade {
    /**
     * RestTemplate object.
     */
    private RestTemplate restTemplate = new RestTemplate();
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.
            getLogger(TodoFacade.class);

    /**
     * Get Todos from https://jsonplaceholder.typicode.com.
     */
    public void getTodos() {
        String url = "https://jsonplaceholder.typicode.com/todos";
        String response = restTemplate.getForObject(url, String.class);
        LOGGER.debug(response);
    }
}
