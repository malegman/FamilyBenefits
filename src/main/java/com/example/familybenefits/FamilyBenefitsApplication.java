package com.example.familybenefits;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FamilyBenefitsApplication {

  public static void main(String[] args) {
    PropertyConfigurator.configure("classpath:log4j.properties");
    SpringApplication.run(FamilyBenefitsApplication.class, args);
  }
}
