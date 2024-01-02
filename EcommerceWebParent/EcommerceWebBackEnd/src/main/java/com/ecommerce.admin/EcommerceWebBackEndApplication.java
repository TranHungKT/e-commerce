package com.ecommerce.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.ecommerce.common.entity", "com.ecommerce.admin.user"})

public class EcommerceWebBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceWebBackEndApplication.class, args);
    }

}
