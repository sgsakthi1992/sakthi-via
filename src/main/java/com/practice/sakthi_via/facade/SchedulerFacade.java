package com.practice.sakthi_via.facade;

import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.model.CurrencyConverter;
import com.practice.sakthi_via.model.Employee;
import com.practice.sakthi_via.model.Mail;
import com.practice.sakthi_via.model.RatesRegister;
import com.practice.sakthi_via.repository.RatesRegisterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
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
     * Scheduler mail subject.
     */
    private static final String MAIL_SUBJECT = "<SAKTHI-VIA> Currency Rate "
            + "as of " + LocalDate.now();
    /**
     * Scheduler mail template.
     */
    private static final String MAIL_TEMPLATE = "schedulerMailTemplate";
    /**
     * EmailService object.
     */
    private EmailService emailService;
    /**
     * RatesRegisterRepository object.
     */
    private RatesRegisterRepository registerRepository;
    /**
     * CurrencyConverterFacade object.
     */
    private CurrencyConverterFacade currencyConverterFacade;

    /**
     * Setter for RatesRegisterRepository object.
     *
     * @param registerRepository RatesRegisterRepository object
     */
    @Autowired
    public void setRegisterRepository(
            final RatesRegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    /**
     * Setter for CurrencyConverterFacade object.
     *
     * @param currencyConverterFacade CurrencyConverterFacade object
     */
    @Autowired
    public void setCurrencyConverterFacade(
            final CurrencyConverterFacade currencyConverterFacade) {
        this.currencyConverterFacade = currencyConverterFacade;
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
    @Scheduled(cron = "${via.scheduler.cron.value}")
    public void getScheduledCurrencyRate() {
        List<RatesRegister> ratesRegisters = registerRepository.findAll();
        LOGGER.debug("Rates registers: {}", ratesRegisters);
        ratesRegisters.forEach(ratesRegister -> {
            Employee employee = ratesRegister.getEmployee();
            LOGGER.debug("Employee: {}", employee);
            CurrencyConverter currencyRate = currencyConverterFacade
                    .getCurrencyRateWithTarget(ratesRegister.getBase(),
                            ratesRegister.getTarget());
            try {
                Map<String, Object> content = new HashMap<>();
                content.put("name", employee.getName());
                content.put("base", ratesRegister.getBase());
                content.put("targets", currencyRate.getRates());
                Mail mail = new Mail(employee.getEmail(),
                        MAIL_SUBJECT, content, MAIL_TEMPLATE);
                emailService.sendMail(mail);
            } catch (MessagingException e) {
                LOGGER.error("Exception in Schedule Mail", e);
            }
        });
    }
}
