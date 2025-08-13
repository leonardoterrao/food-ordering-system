package com.leonardoterrao.ordering.system.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.leonardoterrao.ordering.system.order.service.dataaccess")
@EntityScan(basePackages = "com.leonardoterrao.ordering.system.order.service.dataaccess")
@SpringBootApplication(scanBasePackages = "com.leonardoterrao.ordering.system")
public class OrderServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
