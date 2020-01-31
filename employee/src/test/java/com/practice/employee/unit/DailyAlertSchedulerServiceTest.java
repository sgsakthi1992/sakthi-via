package com.practice.employee.unit;

import com.practice.currencyconverter.facade.CurrencyConverterFacade;
import com.practice.currencyconverter.model.CurrencyConverter;
import com.practice.employee.service.DailyAlertSchedulerService;
import com.practice.employee.model.Employee;
import com.practice.employee.model.RatesRegister;
import com.practice.employee.repository.RatesRegisterRepository;
import com.practice.message.factory.AbstractFactory;
import com.practice.message.model.Content;
import com.practice.message.service.MessagingService;
import com.practice.message.service.impl.EmailService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyAlertSchedulerServiceTest {

    @InjectMocks
    DailyAlertSchedulerService dailyAlertSchedulerService;

    @Mock
    AbstractFactory<MessagingService> abstractFactory;

    @Mock
    RatesRegisterRepository registerRepository;

    @Mock
    CurrencyConverterFacade currencyConverterFacade;

    @Mock
    EmailService emailService;

    private List<RatesRegister> ratesRegisters;
    private CurrencyConverter converterHuf;
    private CurrencyConverter converterInr;

    void setup() {
        ratesRegisters = new ArrayList<>();
        ratesRegisters.add(new RatesRegister(1,
                new Employee(40000L, "Employee",
                        "employee", "employee@gmail.com", "+111111111", 25),
                "HUF", Set.of("INR", "EUR")));
        ratesRegisters.add(new RatesRegister(2,
                new Employee(40001L, "Employee1",
                        "employee1", "employee1@gmail.com", "+111111111", 25),
                "INR", Set.of("HUF", "USD")));

        Map<String, Double> ratesHuf = new HashMap<>();
        ratesHuf.put("EUR", 0.0029798266);
        ratesHuf.put("INR", 0.2352772729);

        converterHuf = new CurrencyConverter();
        String baseHuf = "HUF";
        converterHuf.setBase(baseHuf);
        converterHuf.setDate(LocalDate.now());
        converterHuf.setRates(ratesHuf);

        Map<String, Double> ratesInr = new HashMap<>();
        ratesInr.put("HUF", 4.2503042807);
        ratesInr.put("USD", 0.0140684704);

        converterInr = new CurrencyConverter();
        String baseInr = "INR";
        converterInr.setBase(baseInr);
        converterInr.setDate(LocalDate.now());
        converterInr.setRates(ratesInr);
    }

    @Test
    void getScheduledCurrencyRate() {
        //GIVEN
        setup();
        ArgumentCaptor<Content> captor = ArgumentCaptor.forClass(Content.class);
        when(registerRepository.findAll()).thenReturn(ratesRegisters);
        when(currencyConverterFacade.getCurrencyRateWithTarget("HUF", Set.of("INR", "EUR")))
                .thenReturn(converterHuf);
        when(currencyConverterFacade.getCurrencyRateWithTarget("INR", Set.of("HUF", "USD")))
                .thenReturn(converterInr);
        when(abstractFactory.create("email"))
                .thenReturn(emailService);

        //WHEN
        dailyAlertSchedulerService.dailyEmailAlertScheduler();

        //THEN
        verify(abstractFactory.create("email"), times(ratesRegisters.size())).send(captor.capture());

        Assertions.assertThat(captor.getAllValues().get(0))
                .matches(mail -> mail.getTo().contentEquals(ratesRegisters.get(0).getEmployee().getEmail()))
                .matches(mail -> mail.getBody().get("base").toString().contentEquals(ratesRegisters.get(0).getBase()))
                .matches(mail -> mail.getBody().get("targets").toString().contentEquals(converterHuf.getRates().toString()));

        Assertions.assertThat(captor.getAllValues().get(1))
                .matches(mail -> mail.getTo().contentEquals(ratesRegisters.get(1).getEmployee().getEmail()))
                .matches(mail -> mail.getBody().get("base").toString().contentEquals(ratesRegisters.get(1).getBase()))
                .matches(mail -> mail.getBody().get("targets").toString().contentEquals(converterInr.getRates().toString()));
    }
}