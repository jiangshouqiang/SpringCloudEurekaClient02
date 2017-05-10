package com.gr.jiang.spring.cloud.dto.impl;

import com.gr.jiang.spring.cloud.dto.IResponseDto;

/**
 * Created by jiang on 2017/5/9.
 */
public class OrgResponseDto extends IResponseDto {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
