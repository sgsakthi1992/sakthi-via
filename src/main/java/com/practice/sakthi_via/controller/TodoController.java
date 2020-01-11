package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.facade.TodoFacade;
import com.practice.sakthi_via.model.Todo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TodoController {
    /**
     * TodoFacade object.
     */
    private TodoFacade todoFacade;

    /**
     * Parameterized constructor to bind TodoFacade object.
     *
     * @param todoFacade TodoFacade object
     */
    public TodoController(final TodoFacade todoFacade) {
        this.todoFacade = todoFacade;
    }

    /**
     * API to get Todos from external source.
     *
     * @return ResponseEntity with Users List
     */
    @GetMapping("/todos")
    public ResponseEntity getTodos() {
        List<Todo> todos = todoFacade.getTodos();
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }

    /**
     * API to get Todo for particular user.
     *
     * @param userId user id
     * @return list of todo's
     */
    @GetMapping("/todos/userId")
    public ResponseEntity getTodosById(@PathVariable final int userId) {
        List<Todo> todos = todoFacade.getTodosById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }
}
