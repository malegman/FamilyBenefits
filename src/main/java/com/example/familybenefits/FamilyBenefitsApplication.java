package com.example.familybenefits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.http.HttpClient;

@SpringBootApplication
public class FamilyBenefitsApplication {

  public static void main(String[] args) {
    SpringApplication.run(FamilyBenefitsApplication.class, args);
  }
}
