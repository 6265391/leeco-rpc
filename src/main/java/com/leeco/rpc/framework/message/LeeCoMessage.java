package com.leeco.rpc.framework.message;

/**
 * 自定义协议栈 -> 消息定义
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 16:37
 */
public final class LeeCoMessage {

    /** 消息头 */
    private Header header;

    /** 消息体 */
    private Object body;

    public final Header getHeader() {
        return header;
    }

    public final void setHeader(Header header) {
        this.header = header;
    }

    public final Object getBody() {
        return body;
    }

    public final void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "LeeCoMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }

}
