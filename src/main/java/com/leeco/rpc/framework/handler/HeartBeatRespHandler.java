package com.leeco.rpc.framework.handler;

import com.leeco.rpc.framework.LeeCoRpcApplication;
import com.leeco.rpc.framework.core.MessageType;
import com.leeco.rpc.framework.protocol.Header;
import com.leeco.rpc.framework.protocol.LeeCoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 心跳应答消息
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 18:31
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LeeCoMessage message = (LeeCoMessage) msg;
        // 握手成功 主动发送心跳消息
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()){
            LeeCoRpcApplication.logger.info(">>>>> receive client heart beat message : " + message + " <<<<<");
            LeeCoMessage heartBeat = buildHeartBeat();
            LeeCoRpcApplication.logger.info(">>>>> send heart beat response message to client : " + heartBeat + " <<<<<");
            ctx.writeAndFlush(heartBeat);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private LeeCoMessage buildHeartBeat(){
        LeeCoMessage message = new LeeCoMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.fireExceptionCaught(cause);
    }
}
