/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url = jdbc:h2:mem:test",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
        "spring.datasource.driverClassName = org.h2.Driver"
})
public class TodoAPITest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetTodos() throws Exception {
        //GIVEN
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/todos"));

        //THEN
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testGetTodosById() throws Exception {
        //GIVEN
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/todos/1")).andDo(print());

        //THEN
        resultActions.andExpect(status().isOk());
    }
}
