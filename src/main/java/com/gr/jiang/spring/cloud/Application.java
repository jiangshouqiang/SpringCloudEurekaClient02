package com.gr.jiang.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by jiang on 2017/3/10.
 */
@SpringBootApplication
@EnableEurekaClient
public class Application {
    public static void main(String [] args){
        SpringApplication.run(Application.class,args);
    }
}
