package com.practice.sakthi_via.facade;

import com.practice.sakthi_via.model.Todo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoFacade {
    /**
     * RestTemplate object.
     */
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Get Todos from https://jsonplaceholder.typicode.com.
     *
     * @return list of todos from external resource
     */
    public List<Todo> getTodos() {
        String url = "https://jsonplaceholder.typicode.com/todos";
        Todo[] response = restTemplate.getForObject(url, Todo[].class);
        return Optional.ofNullable(response).map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    /**
     * @param userId User Id
     * @return Todos fo that user
     */
    public List<Todo> getTodosById(final int userId) {
        List<Todo> todos = getTodos();
        return todos.stream()
                .filter(todo -> todo.getUserId() == userId)
                .collect(Collectors.toList());
    }
}
