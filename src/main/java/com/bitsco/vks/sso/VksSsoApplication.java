package com.bitsco.vks.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = "com.bitsco.*")
public class VksSsoApplication {

    public static void main(String[] args) {
        Environment env = SpringApplication.run(VksSsoApplication.class, args).getEnvironment();
        String appName = env.getProperty("spring.application.name").toUpperCase();
        String port = env.getProperty("server.port");
        System.out.println("-------------------------START " + appName + " Application------------------------------");
        System.out.println("   Application         : " + appName);
        System.out.println("   Url swagger-ui      : http://localhost:" + port + "/swagger-ui.html");
        System.out.println("-------------------------START SUCCESS " + appName + " Application------------------------------");
    }

}
