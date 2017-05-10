package com.gr.jiang.spring.cloud.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;


public class JrnGenerator {

    public static AtomicLong seq = new AtomicLong(0);

    public static String genJrnNo() {

        String nodId = SystemConstants.INSTANCE_NODE;
        long a = seq.incrementAndGet();
        a %= 10000;
        return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())
                + StringUtils.leftPad(String.valueOf(a), 4, "0") + StringUtils.trimToEmpty(nodId);
    }

    public static AtomicLong msgIdSeq = new AtomicLong(0);

    public static String genMsgId() {

        String insId = SystemConstants.INSTANCE_ID;
        String nodId = SystemConstants.INSTANCE_NODE;
        long a = msgIdSeq.incrementAndGet();
        a %= 10000;
        return StringUtils.trimToEmpty(insId) + StringUtils.trimToEmpty(nodId)
                + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())
                + StringUtils.leftPad(String.valueOf(a), 4, "0");
    }
}
