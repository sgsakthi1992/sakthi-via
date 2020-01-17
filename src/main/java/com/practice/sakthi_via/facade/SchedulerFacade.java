package com.practice.sakthi_via.facade;

import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.model.Mail;
import com.practice.sakthi_via.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchedulerFacade {
    /**
     * Logger Object to log the details.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SchedulerFacade.class);
    /**
     * Fixed rate for scheduler.
     */
    private static final long FIXED_RATE = 300000;
    /**
     * EmployeeRepository object.
     */
    private EmployeeRepository employeeRepository;
    /**
     * EmailService object.
     */
    private EmailService emailService;

    /**
     * Setter for EmployeeRepository object.
     *
     * @param employeeRepository EmployeeRepository object
     */
    @Autowired
    public void setEmployeeRepository(
            final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Setter for EmailService object.
     *
     * @param emailService EmailService object
     */
    @Autowired
    public void setEmailService(final EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Method to schedule the currency rate.
     */
    @Scheduled(fixedRate = FIXED_RATE)
    public void getScheduledCurrencyRate() {
        List<Employee> employees = employeeRepository.findAll();
        employees.forEach(
                employee -> {
                    try {
                        Map<String, Object> content = new HashMap<>();
                        content.put("name", employee.getName());
                        content.put("username", employee.getUsername());
                        content.put("age", employee.getAge());
                        content.put("email", employee.getEmail());
                        Mail mail = new Mail(employee.getEmail(),
                                "Schedule Mail", content);
                        emailService.sendMail(mail);
                    } catch (MessagingException e) {
                        LOGGER.error("Exception in Schedule Mail", e);
                    }
                }
        );
    }
}
