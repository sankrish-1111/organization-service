package com.santhanam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.santhanam")
public class OrganizationGroupServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrganizationGroupServiceApplication.class, args);
    }
}
