package com.practice.sakthi_via.integration;

import com.practice.sakthi_via.facade.CurrencyConverterFacade;
import com.practice.sakthi_via.facade.SchedulerFacade;
import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.repository.RatesRegisterRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@SpringBootTest
@ActiveProfiles("test")
class SchedulerTest {

    @SpyBean
    SchedulerFacade schedulerFacade;

    @MockBean
    EmailService emailService;

    @MockBean
    RatesRegisterRepository registerRepository;

    @MockBean
    CurrencyConverterFacade currencyConverterFacade;

    @Test
    void testScheduler() {
        Awaitility.await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> Mockito.verify(schedulerFacade,
                        Mockito.atMost(5)).dailyEmailAlertScheduler());
    }
}
