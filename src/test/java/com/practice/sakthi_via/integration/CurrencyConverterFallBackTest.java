/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0, stubs = "classpath:/stubs/")
@TestPropertySource(properties = {
        "spring.datasource.url = jdbc:h2:mem:test",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
        "spring.datasource.driverClassName = org.h2.Driver",
        "via.countries.api.url = http://localhost:${wiremock.server.port}/api/invalidCurrencies.json",
        "via.currencyrate.api.url = http://localhost:${wiremock.server.port}/invalidLatest?base=%s"
})
@ActiveProfiles("test")
class CurrencyConverterFallBackTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testFallBackDefaultCountryAndCurrency() throws Exception {
        //GIVEN
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/countries"));

        //THEN
        resultActions.andExpect(status().isOk())
        .andExpect(content().json("{\"HUF\":\"Hungarian Forint\"}"));
    }

    @Test
    void testGetCurrencyRate() throws Exception {
        //GIVEN
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/rates?base=HUF"));

        //THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json("{base:HUF,date:"+ LocalDate.now() +"}"));
    }
}
