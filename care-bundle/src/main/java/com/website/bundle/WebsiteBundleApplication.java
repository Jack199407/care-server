package com.website.bundle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.website"})
public class WebsiteBundleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsiteBundleApplication.class, args);
    }

}
