package com.practice.sakthi_via.controller;

import com.practice.sakthi_via.facade.TodoFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        todoFacade.getTodos();
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
