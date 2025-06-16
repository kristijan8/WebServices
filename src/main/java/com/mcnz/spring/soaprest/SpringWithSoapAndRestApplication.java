package com.mcnz.spring.soaprest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringWithSoapAndRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWithSoapAndRestApplication.class, args);
    }

}
