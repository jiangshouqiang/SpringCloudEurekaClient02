package com.gr.jiang.spring.cloud.service;

import com.gr.jiang.spring.cloud.dto.impl.OrgRequestDto;
import com.gr.jiang.spring.cloud.dto.impl.OrgResponseDto;
import org.springframework.stereotype.Service;

/**
 * Created by jiang on 2017/5/9.
 */
@Service
public class OrgService {

    public OrgResponseDto handle(OrgRequestDto requestDto){
        OrgResponseDto responseDto = new OrgResponseDto();
        String id = requestDto.getId();
        responseDto.setResult(id+" result");
        return responseDto;
    }
}
