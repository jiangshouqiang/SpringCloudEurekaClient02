package com.gr.jiang.spring.cloud.controller;

import com.gr.jiang.spring.cloud.dto.impl.OrgRequestDto;
import com.gr.jiang.spring.cloud.dto.impl.OrgResponseDto;
import com.gr.jiang.spring.cloud.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by jiang on 2017/3/10.
 */
@RestController
@RequestMapping("/org")
public class orgController {
    @Autowired
    OrgService orgService;

    @Value("${foo:default-value}")
    String bar;

    @RequestMapping("/detail")
    public String org(){
        OrgRequestDto reqDto = new OrgRequestDto();
        reqDto.setId("Id");
        OrgResponseDto resDto = orgService.handle(reqDto);

//        CloudFactory cloudFactory = new CloudFactory();
//        Cloud cloud = cloudFactory.getCloud();
//        List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
//        List<ServiceInfo> databaseInfos = cloud.getServiceInfos(DataSource.class);

        return "## org ##" + resDto.getResult();
    }
}
