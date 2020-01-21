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
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
     * CacheManager object.
     */
    private CacheManager cacheManager;

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
     * Setter for CacheManager object.
     *
     * @param cacheManager CacheManager object.
     */
    @Autowired
    public void setCacheManager(final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Method to schedule the currency rate.
     */
    @Scheduled(cron = "${via.scheduler.cron.value}")
    public void getScheduledCurrencyRate() {
        List<RatesRegister> ratesRegisters = registerRepository.findAll();
        LOGGER.debug("Rates registers: {}", ratesRegisters);
        Map<String, Map<Set<String>, List<RatesRegister>>> registersGroupBy =
                ratesRegisters.stream().collect(
                        Collectors.groupingBy(RatesRegister::getBase,
                                Collectors.groupingBy(RatesRegister::getTarget)
                        ));
        LOGGER.debug("Rates registers Group by: {}", registersGroupBy);
        registersGroupBy.forEach((key, value) ->
                value.forEach((detailsKey, detailsValue) -> {
            StringJoiner toAddress = new StringJoiner(",");
            detailsValue.forEach(ratesRegister -> {
                Employee employee = ratesRegister.getEmployee();
                LOGGER.debug("Employee: {}", employee);
                toAddress.add(employee.getEmail());
            });
            CurrencyConverter currencyRate = currencyConverterFacade
                    .getCurrencyRateWithTarget(key,
                            detailsKey);
            try {
                Map<String, Object> content = new HashMap<>();
                content.put("base", key);
                content.put("targets", currencyRate.getRates());
                LOGGER.debug("To Addresses: {}", toAddress.toString());
                Mail mail = new Mail(toAddress.toString(),
                        MAIL_SUBJECT, content, MAIL_TEMPLATE);
                emailService.sendMail(mail);
            } catch (MessagingException e) {
                LOGGER.error("Exception in Schedule Mail", e);
            }
        }));
    }

    /**
     * To clear all the caches at regular intervals.
     */
    @Scheduled(fixedRateString = "${via.scheduler.cache.evict.value}")
    public void evictAllCachesAtIntervals() {
        LOGGER.debug("Caches are: {}", cacheManager.getCacheNames());
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(
                        cacheManager.getCache(cacheName)).clear());
        LOGGER.debug("Caches cleared!");
    }
}
