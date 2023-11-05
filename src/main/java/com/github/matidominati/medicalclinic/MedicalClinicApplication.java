package com.github.matidominati.medicalclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class MedicalClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalClinicApplication.class, args);

    }
}