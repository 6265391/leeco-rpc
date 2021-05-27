package com.leeco.rpc.framework.protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义消息头
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 16:39
 */
public final class Header {

    /**
     * 消息校验码 三部分组成
     * 1. 0xabef:固定值 表明该消息是netty协议消息 2个字节
     * 2. 01主版本号 1~255 1个字节
     * 2. 01次版本号 1~255 1个字节
     */
    private int crcCode = 0xabef0101;

    /**
     * 消息长度(包括消息头和消息体)
     */
    private int length;

    /**
     * 集群内唯一ID 由会话ID生成器生成
     */
    private long sessionId;

    /**
     * 消息类型
     * 0: 业务请求消息
     * 1: 业务响应消息
     * 2: 业务ONE WAY消息(既是请求 又是响应)
     * 3: 握手请求消息
     * 4: 握手应答消息
     * 5: 心跳请求消息
     * 6: 心跳应答消息
     */
    private byte type;

    /**
     * 消息优先级 0~255
     */
    private byte priority;

    /**
     * 可选字段 用于扩展消息头
     */
    private Map<String,Object> attachment = new ConcurrentHashMap<>(0);

    public final int getCrcCode() {
        return crcCode;
    }

    public final void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public final int getLength() {
        return length;
    }

    public final void setLength(int length) {
        this.length = length;
    }

    public final long getSessionId() {
        return sessionId;
    }

    public final void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public final byte getType() {
        return type;
    }

    public final void setType(byte type) {
        this.type = type;
    }

    public final byte getPriority() {
        return priority;
    }

    public final void setPriority(byte priority) {
        this.priority = priority;
    }

    public final Map<String, Object> getAttachment() {
        return attachment;
    }

    public final void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionId=" + sessionId +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }

}
