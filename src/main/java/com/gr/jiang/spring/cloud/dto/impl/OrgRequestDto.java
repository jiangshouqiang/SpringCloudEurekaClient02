package com.gr.jiang.spring.cloud.dto.impl;

import com.gr.jiang.spring.cloud.dto.IRequestDto;

/**
 * Created by jiang on 2017/5/9.
 */
public class OrgRequestDto extends IRequestDto {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
