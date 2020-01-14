/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
@ActiveProfiles("test")
class CurrencyConverterAPITest {
    @Autowired
    private MockMvc mockMvc;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void testGetCountriesAndCurrencies() throws Exception {
        //GIVEN
        givenThat(WireMock.get(urlPathEqualTo("/api/currencies.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBodyFile("json/countries.json")));
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/countries")).andDo(print());

        //THEN
        resultActions.andExpect(status().isOk());
    }

    @Test
    void testGetCountryForCurrencyCode() throws Exception {
        //GIVEN
        givenThat(WireMock.get(urlPathEqualTo("/api/currencies.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBodyFile("json/countries.json")));
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/country/HUF")).andDo(print());

        //THEN
        resultActions.andExpect(status().isOk());
    }

    @Test
    void testGetCurrencyRate() throws Exception {
        //GIVEN
        givenThat(WireMock.get(urlPathEqualTo("/latest"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBodyFile("json/currencyRate.json")));
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/rates?base=HUF")).andDo(print());

        //THEN
        resultActions.andExpect(status().isOk());
    }

    @Test
    void getHighestAndLowestCurrencyRates() throws Exception {
        //GIVEN
        givenThat(WireMock.get(urlPathEqualTo("/latest"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBodyFile("json/currencyRate.json")));
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/highestCurrencyRate?base=HUF")).andDo(print());

        //THEN
        resultActions.andExpect(status().isOk());
    }
}
