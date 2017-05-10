package com.gr.jiang.spring.cloud.dto;

import com.gr.jiang.spring.cloud.config.SystemMessageContext;

import java.util.EmptyStackException;

/**
 * 与Service数据传输的请求基类 Created by GaoPopo on 16/7/22.
 */
public class IRequestDto extends IBaseDto {
    /**
     * 前置消息id
     */
    private String preMsgId;

    public IRequestDto() {
        try {
            this.preMsgId = SystemMessageContext.getCurrentContext().getRequestId();
        } catch (NullPointerException | EmptyStackException e) {
            SystemMessageContext ctx = SystemMessageContext.createContext("currentContext", null);
            SystemMessageContext.pushCurrentContext(ctx);
            this.preMsgId = SystemMessageContext.getCurrentContext().getRequestId();
        }
    }

    public String getPreMsgId() {
        return preMsgId;
    }

    public void setPreMsgId(String preMsgId) {
        this.preMsgId = preMsgId;
    }
}
