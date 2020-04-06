package com.leeco.rpc.framework.core;

/**
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 18:11
 */
public enum MessageType {

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
    LOGIN_REQ(3),

    /**
     * 握手应答消息
     */
    LOGIN_RESP(4),

    /**
     * 心跳请求消息
     */
    HEARTBEAT_REQ(5),

    /**
     * 心跳应答消息
     */
    HEARTBEAT_RESP(6);

    Integer value;

    public byte value(){
        return value.byteValue();
    }

    MessageType(Integer i) {
        this.value = i;
    }

}
