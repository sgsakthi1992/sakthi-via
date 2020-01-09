package com.practice.sakthi_via.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.practice.sakthi_via.constants.Constants;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeAPITest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;

    public Optional<Employee> createEmployee(String name, String email, String username, Integer age) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employee.setUsername(username);
        employee.setAge(age);
        employeeRepository.save(employee);
        return employeeRepository.findById(employee.getId());
    }
    private static GreenMail greenMail;
    private static void setupSMTP() {
        greenMail = new GreenMail(new ServerSetup(2525, "127.0.0.1", "smtp"));
        greenMail.start();
    }
    private static void tearDownSMTP() {
        greenMail.stop();
    }

    @Test
    @Order(1)
    public void testGetEmployees() throws Exception {
        //GIVEN
        Optional<Employee> employee = createEmployee
                ("Employee1", "employee1@gmail.com", "employee1", 27);
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees"));
        //THEN
        validateOkResponse(resultActions)
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(employee.get().getName())));
    }

    @Test
    public void testPostEmployee() throws Exception {
        //GIVEN
        setupSMTP();

        Employee employee = new Employee();
        employee.setName("Employee 2");
        employee.setEmail("sgsakthi1992@gmail.com");
        employee.setUsername("employee2");
        employee.setAge(25);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(employee);

        //WHEN
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        //THEN
        validateOkResponse(resultActions).andExpect(jsonPath("name", is(employee.getName())));
        validateEmailResponse(employee);

        tearDownSMTP();
    }

    @Test
    public void testCreateWithExistingUserName() throws Exception {
        //GIVEN
        Optional<Employee> employee = createEmployee("Employee3", "employee3@gmail.com",
                "employee3", 27);
        Employee newEmployee = new Employee();
        newEmployee.setName("Employee 4");
        newEmployee.setEmail("employee4@gmail.com");
        newEmployee.setUsername("employee3");
        newEmployee.setAge(25);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(newEmployee);

        //WHEN
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        //THEN
        validateBadRequestResponse(resultActions);
    }

    private ResultActions validateOkResponse(ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk());
    }

    private void validateBadRequestResponse(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isBadRequest());
    }

    private void validateEmailResponse(Employee employee) throws MessagingException, IOException {
        boolean ok = greenMail.waitForIncomingEmail(1);
        if(ok) {
            MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
            assertEquals(employee.getEmail(), receivedMessage.getAllRecipients()[0].toString());
            assertEquals(Constants.EMAIL_SUBJECT, receivedMessage.getSubject());
        } else{
            fail("Email not sent");
        }
    }
}
