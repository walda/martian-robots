package com.guidesmiths.robots.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class MartianRobotsConfig {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

}
