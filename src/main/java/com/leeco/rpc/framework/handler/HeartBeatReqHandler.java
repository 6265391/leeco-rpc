package com.leeco.rpc.framework.handler;

import com.leeco.rpc.framework.LeeCoRpcApplication;
import com.leeco.rpc.framework.core.MessageType;
import com.leeco.rpc.framework.protocol.Header;
import com.leeco.rpc.framework.protocol.LeeCoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 心跳请求消息
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 18:31
 */
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LeeCoMessage message = (LeeCoMessage) msg;
        // 握手成功 主动发送心跳消息
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx),0,5000, TimeUnit.MILLISECONDS);
        }else{
            if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()){
                LeeCoRpcApplication.logger.info(">>>>> client receive server heart beat message : " + message + " <<<<<");
            }else{
                ctx.fireChannelRead(msg);
            }
        }
    }

    private class HeartBeatTask implements Runnable{

        private final ChannelHandlerContext ctx;

        HeartBeatTask(ChannelHandlerContext ctx){
            this.ctx = ctx;
        }

        @Override
        public void run() {
            LeeCoMessage heartBeat = buildHeartBeat();
            LeeCoRpcApplication.logger.info(">>>>> client send heart beat message : " + heartBeat + " <<<<<");
            ctx.writeAndFlush(heartBeat);
        }

        private LeeCoMessage buildHeartBeat(){
            LeeCoMessage message = new LeeCoMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (heartBeat != null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
