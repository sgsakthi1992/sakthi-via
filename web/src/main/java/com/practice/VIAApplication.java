package com.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.practice"})
public class VIAApplication {

  public static void main(String[] args) {
    SpringApplication.run(VIAApplication.class);
  }

}
