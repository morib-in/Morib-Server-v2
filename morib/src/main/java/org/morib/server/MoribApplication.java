package org.morib.server;

import org.morib.server.global.common.SecretProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(SecretProperties.class)
@EnableScheduling
public class MoribApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoribApplication.class, args);
    }

}
