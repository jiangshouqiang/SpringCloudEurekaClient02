
package com.gr.jiang.spring.cloud.dto;

import com.gr.jiang.spring.cloud.config.SystemMessageContext;

import java.util.EmptyStackException;

/**
 * 与service进行消息传输的响应基类 Created by GaoPopo on 16/7/22.
 */
public class IResponseDto extends IBaseDto {
    // 当前消息id
    private String msgId = SystemMessageContext.getCurrentContext().getRequestId();

    // 默认返回成功
    private String retCode = "0";

    // 默认返回成功
    private String retMsg = "success";

    public IResponseDto() {
        try {
            this.msgId = SystemMessageContext.getCurrentContext().getRequestId();
        } catch (NullPointerException | EmptyStackException e) {
            SystemMessageContext ctx = SystemMessageContext.createContext("currentContext", null);
            SystemMessageContext.pushCurrentContext(ctx);
            this.msgId = SystemMessageContext.getCurrentContext().getRequestId();
        }
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
}
