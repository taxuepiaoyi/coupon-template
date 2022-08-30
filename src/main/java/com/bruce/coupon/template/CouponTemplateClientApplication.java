package com.bruce.coupon.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bruce"})
public class CouponTemplateClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponTemplateClientApplication.class, args);
    }
}
