package com.abm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerGit {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerGit.class);
    }
}