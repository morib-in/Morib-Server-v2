package org.morib.server;

import org.morib.server.global.common.SecretProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecretProperties.class)
public class MoribApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoribApplication.class, args);
    }

}
