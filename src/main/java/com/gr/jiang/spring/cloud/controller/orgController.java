package com.gr.jiang.spring.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jiang on 2017/3/10.
 */
@RestController
@RequestMapping("/org")
public class orgController {

    @Value("${foo:default-value}")
    String bar;

    @RequestMapping("/detail")
    public String org(){
        return "## org ##" + bar;
    }
}
