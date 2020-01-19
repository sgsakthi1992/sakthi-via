package com.practice.sakthi_via.unit;

import com.practice.sakthi_via.facade.CurrencyConverterFacade;
import com.practice.sakthi_via.facade.SchedulerFacade;
import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.repository.RatesRegisterRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

@SpringBootTest
@ActiveProfiles("test")
class SchedulerFacadeTest {

    @SpyBean
    SchedulerFacade schedulerFacade;

    @MockBean
    EmailService emailService;

    @MockBean
    RatesRegisterRepository ratesRegisterRepository;

    @MockBean
    CurrencyConverterFacade currencyConverterFacade;

    @Test
    void testScheduler() {
        Awaitility.await().atMost(Duration.ofSeconds(20))
                .untilAsserted(() -> Mockito.verify(schedulerFacade, Mockito.atLeastOnce()).getScheduledCurrencyRate());
    }
}
