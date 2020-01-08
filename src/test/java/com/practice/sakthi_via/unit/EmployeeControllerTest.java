package com.practice.sakthi_via.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.practice.sakthi_via.constants.Constants;
import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.mail.impl.EmailServiceImpl;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.repository.EmployeeRepository;
import com.practice.sakthi_via.facade.EmployeeFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class EmployeeControllerTest {

    @TestConfiguration
    static class EmployeeServiceTestContextConfiguration {
        @Bean
        public EmployeeFacade employeeService() {
            return new EmployeeFacade();
        }

        @Bean
        public EmailService emailService() {
            return new EmailServiceImpl();
        }

        @Bean
        public JavaMailSender javaMailSender() {
            return new JavaMailSenderImpl();
        }
    }

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeRepository employeeRepository;

    @MockBean
    EmailService emailService;

    Employee employee = new Employee();

    @BeforeEach
    public void setup() {
        employee.setId((long) 40000);
        employee.setName("Sakthi");
        employee.setEmail("sgsakthi1992@gmail.com");
        employee.setUsername("sgsakthi");
        employee.setAge(27);
    }

    @Test
    public void testGetEmployee() throws Exception {
        //GIVEN
        when(employeeRepository.findAll()).thenReturn(Stream.of(employee).collect(Collectors.toList()));
        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees"));
        //THEN
        validateOkResponse(resultActions)
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(employee.getName())));
    }

    @Test
    public void testPostEmployee() throws Exception {
        //GIVEN
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);
        when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.ofNullable(employee));
        doNothing().when(emailService).sendMail(employee.getEmail(),
                "Employee created in SAKTHI-VIA", employee);

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
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        //GIVEN
        when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.ofNullable(employee));

        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees/" + employee.getId()));

        //THEN
        validateOkResponse(resultActions).andExpect(jsonPath("name", is(employee.getName())));
    }

    @Test
    public void testGetEmployeeWithInvalidId() throws Exception {
        //GIVEN
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.empty());

        //WHEN
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees/" + id));

        //THEN
        validateNotFoundResponse(resultActions, Constants.EMPLOYEE_ID_NOT_FOUND);
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        //GIVEN
        when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.ofNullable(employee));
        doNothing().when(employeeRepository).deleteById(employee.getId());

        //WHEN
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/employees/" + employee.getId()));

        //THEN
        validateOkResponse(resultActions).andExpect(content().string(containsString("Success")));
    }

    @Test
    public void testDeleteEmployeeWithInvalidId() throws Exception {
        //GIVEN
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.empty());

        //WHEN
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/employees/" + id));

        //THEN
        validateNotFoundResponse(resultActions, Constants.EMPLOYEE_ID_NOT_FOUND);
    }

    @Test
    public void testUpdateEmployeeEmailById() throws Exception {
        //GIVEN
        when(employeeRepository.findById(employee.getId())).thenReturn(java.util.Optional.ofNullable(employee));
        when(employeeRepository.updateEmployeeEmail(employee.getId(), "newemail@gmail.com")).thenReturn(1);

        //WHEN
        ResultActions resultActions = mockMvc.perform(put("/api/v1/employees/" + employee.getId())
                .param("email", "newemail@gmail.com"));

        //THEN
        validateOkResponse(resultActions).andExpect(content().string(containsString("Success")));
    }

    @Test
    public void testUpdateEmployeeEmailWithInvalidId() throws Exception {
        //GIVEN
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.empty());

        //WHEN
        ResultActions resultActions = mockMvc.perform(put("/api/v1/employees/" + id)
                .param("email", "newemail@gmail.com"));

        //THEN
        validateNotFoundResponse(resultActions, Constants.EMPLOYEE_ID_NOT_FOUND);
    }

    @Test
    public void testUpdateEmployeeEmailWithInvalidEmail() throws Exception {
        //GIVEN
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(java.util.Optional.empty());

        //WHEN
        ResultActions resultActions = mockMvc.perform(put("/api/v1/employees/" + id)
                .param("email", "newemail"));

        //THEN
        String expectedMessage = "Not a well-formed email address";
        validateBadRequestResponse(resultActions, expectedMessage);
    }

    private ResultActions validateOkResponse(ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk());
    }

    private void validateNotFoundResponse(ResultActions resultActions, String expectedMessage) throws Exception {
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("message", containsStringIgnoringCase(expectedMessage)));
    }

    private void validateBadRequestResponse(ResultActions resultActions, String expectedMessage) throws Exception {
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", containsStringIgnoringCase(expectedMessage)));
    }

}
