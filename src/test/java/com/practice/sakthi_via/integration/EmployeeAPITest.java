/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.practice.sakthi_via.model.dto.EmployeeDto;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url = jdbc:h2:mem:test",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
        "spring.datasource.driverClassName = org.h2.Driver"
})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeAPITest {

    private MockMvc mockMvc;

    private EmployeeRepository employeeRepository;

    private static GreenMail greenMail;

    @Autowired
    public void setMockMvc(final MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setEmployeeRepository(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private static void setupSMTP() {
        greenMail = new GreenMail(new ServerSetup(2525, "127.0.0.1", "smtp"));
        greenMail.start();
    }

    private static void tearDownSMTP() {
        greenMail.stop();
    }

    @Sql({"classpath:testDbQuery.sql"})
    @Test
    void testGetEmployees() throws Exception {
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees"));
        //THEN
        validateOkResponse(resultActions)
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testPostEmployee() throws Exception {
        //GIVEN
        setupSMTP();

        EmployeeDto employeeDto = new EmployeeDto("Employee 2",
                "employee2", "employee2@gmail.com", 22);
        String requestJson = convertEmployeeDtoToJson(employeeDto);

        //WHEN
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        //THEN
        validateOkResponse(resultActions).andExpect(jsonPath("name", is(employeeDto.getName())));
        validateEmailResponse(employeeDto);

        tearDownSMTP();
    }

    @Sql({"classpath:testDbQuery.sql"})
    @Test
    void testCreateWithExistingUserName() throws Exception {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 4",
                "employee", "employee4@gmail.com", 22);
        String requestJson = convertEmployeeDtoToJson(employeeDto);

        //WHEN
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        //THEN
        validateBadRequestResponse(resultActions);
    }

    @Test
    void testCreateWithInvalidEmailId() throws Exception {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 4",
                "employee4", "employee4", 22);
        String requestJson = convertEmployeeDtoToJson(employeeDto);

        //WHEN
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        //THEN
        validateBadRequestResponse(resultActions);
    }

    @Test
    void testCreateWithInvalidAge() throws Exception {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 4",
                "employee4", "employee4@gmail.com", 0);
        String requestJson = convertEmployeeDtoToJson(employeeDto);

        //WHEN
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        //THEN
        validateBadRequestResponse(resultActions);
    }

    @Test
    void testCreateWithInvalidUsernameMinSize() throws Exception {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 4",
                "emp", "employee4@gmail.com", 22);
        String requestJson = convertEmployeeDtoToJson(employeeDto);

        //WHEN
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        //THEN
        validateBadRequestResponse(resultActions);
    }

    @Test
    void testCreateWithInvalidUsernameMaxSize() throws Exception {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("Employee 4",
                "employee4_username", "employee4@gmail.com", 22);
        String requestJson = convertEmployeeDtoToJson(employeeDto);

        //WHEN
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        //THEN
        validateBadRequestResponse(resultActions);
    }

    @Test
    void testCreateWithEmptyName() throws Exception {
        //GIVEN
        EmployeeDto employeeDto = new EmployeeDto("",
                "employee4_username", "employee4@gmail.com", 22);
        String requestJson = convertEmployeeDtoToJson(employeeDto);

        //WHEN
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        //THEN
        validateBadRequestResponse(resultActions);
    }

    private String convertEmployeeDtoToJson(EmployeeDto employeeDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(employeeDto);
    }

    private ResultActions validateOkResponse(ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk());
    }

    private void validateBadRequestResponse(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isBadRequest());
    }

    private void validateEmailResponse(EmployeeDto employee) throws MessagingException {
        boolean ok = greenMail.waitForIncomingEmail(1);
        if (ok) {
            MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
            assertEquals(employee.getEmail(), receivedMessage.getAllRecipients()[0].toString());
            assertEquals("Welcome to SAKTHI-VIA!!", receivedMessage.getSubject());
        } else {
            fail("Email not sent");
        }
    }
}
