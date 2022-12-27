package com.practice.employee.service;

import com.practice.currencyconverter.facade.CurrencyConverterFacade;
import com.practice.currencyconverter.model.CurrencyConverter;
import com.practice.employee.model.Employee;
import com.practice.employee.model.RatesRegister;
import com.practice.employee.repository.RatesRegisterRepository;
import com.practice.message.factory.AbstractFactory;
import com.practice.message.model.Content;
import com.practice.message.service.MessagingService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DailyAlertSchedulerService {

  /**
   * Logger Object to log the details.
   */
  private static final Logger LOGGER = LoggerFactory
      .getLogger(DailyAlertSchedulerService.class);
  /**
   * Scheduler mail subject.
   */
  private static final String EMAIL_SUBJECT = "<SAKTHI-VIA> Currency Rate "
      + "as of " + LocalDate.now();
  /**
   * Scheduler mail template.
   */
  private static final String MAIL_TEMPLATE = "schedulerMailTemplate";
  /**
   * Messaging Service object.
   */
  private final AbstractFactory<MessagingService> abstractFactory;
  /**
   * RatesRegisterRepository object.
   */
  private final RatesRegisterRepository registerRepository;
  /**
   * CurrencyConverterFacade object.
   */
  private final CurrencyConverterFacade currencyConverterFacade;

  /**
   * Parameterized constructor to bind the objects.
   *
   * @param abstractFactory         Abstract Factory of type Messaging Service
   * @param registerRepository      RatesRegisterRepository object
   * @param currencyConverterFacade CurrencyConverterFacade object
   */
  public DailyAlertSchedulerService(final AbstractFactory<MessagingService>
      abstractFactory,
      final RatesRegisterRepository
          registerRepository,
      final CurrencyConverterFacade
          currencyConverterFacade) {
    this.abstractFactory = abstractFactory;
    this.registerRepository = registerRepository;
    this.currencyConverterFacade = currencyConverterFacade;
  }

  /**
   * Method to schedule the currency rate.
   */
  @Scheduled(cron = "${via.scheduler.cron.value}")
  public void dailyEmailAlertScheduler() {

    Map<String, Map<Set<String>, List<RatesRegister>>>
        registeredForAlerts = getAlertRegistrationDetails();

    registeredForAlerts.forEach((baseCode, targetDetailsMap) ->
        targetDetailsMap.forEach((targetsSet, ratesRegistersList) ->
            sendMail(baseCode, getLatestRates(baseCode, targetsSet),
                getToAddresses(ratesRegistersList))
        ));
  }

  private Map<String, Map<Set<String>,
      List<RatesRegister>>> getAlertRegistrationDetails() {
    List<RatesRegister> ratesRegisters = registerRepository.findAll();
    LOGGER.debug("Rates registers: {}", ratesRegisters);

    Map<String, Map<Set<String>, List<RatesRegister>>>
        groupByBaseTargets = ratesRegisters.stream().collect(
        Collectors.groupingBy(RatesRegister::getBase,
            Collectors.groupingBy(RatesRegister::getTarget)
        ));
    LOGGER.debug("Rates registers Group by: {}", groupByBaseTargets);

    return groupByBaseTargets;
  }

  private Map<String, Double> getLatestRates(
      final String baseCode,
      final Set<String> targetsSet) {
    CurrencyConverter currencyRate = currencyConverterFacade
        .getCurrencyRateWithTarget(baseCode,
            targetsSet);
    return currencyRate.getRates();
  }

  private StringJoiner getToAddresses(
      final List<RatesRegister> ratesRegistersList) {
    StringJoiner toAddress = new StringJoiner(",");
    ratesRegistersList.forEach(ratesRegister -> {
      Employee employee = ratesRegister.getEmployee();
      LOGGER.debug("Employee: {}", employee);
      toAddress.add(employee.getEmail());
    });
    return toAddress;
  }

  private void sendMail(final String key,
      final Map<String, Double> targets,
      final StringJoiner toAddress) {

    abstractFactory.create("email").send(Content.builder()
        .setTo(toAddress.toString())
        .setSubject(EMAIL_SUBJECT)
        .setBody(Map.of("base", key, "targets", targets))
        .setTemplate(MAIL_TEMPLATE)
        .createMail());

  }

}
