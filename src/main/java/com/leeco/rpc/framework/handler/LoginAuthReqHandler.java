package com.leeco.rpc.framework.handler;

import com.leeco.rpc.framework.LeeCoRpcApplication;
import com.leeco.rpc.framework.core.MessageType;
import com.leeco.rpc.framework.message.Header;
import com.leeco.rpc.framework.message.LeeCoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 握手安全认证
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 18:00
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    private LeeCoMessage buildLoginReq() {
        LeeCoRpcApplication.logger.info(">>>>> client send login request <<<<<");
        LeeCoMessage message = new LeeCoMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LeeCoMessage message = (LeeCoMessage) msg;
        // 如果是握手应答消息 判断是否认证成功
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte)0){
                // 握手失败 关闭连接
                ctx.close();
            }else{
                LeeCoRpcApplication.logger.info(">>>>> Login is OK : " + message + " <<<<<");
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
