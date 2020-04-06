package com.leeco.rpc.framework.handler;

import com.leeco.rpc.framework.LeeCoRpcApplication;
import com.leeco.rpc.framework.core.MessageType;
import com.leeco.rpc.framework.message.Header;
import com.leeco.rpc.framework.message.LeeCoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 握手安全认证
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 18:00
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<>();

    private final String[] whiteList = {"127.0.0.1","192.168.1.37"};

    private LeeCoMessage buildLoginResp(byte result) {
        LeeCoMessage message = new LeeCoMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LeeCoMessage message = (LeeCoMessage) msg;
        LeeCoRpcApplication.logger.info(message.getHeader().toString());
        // 如果是握手请求消息
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()){
            String nodeIndex = ctx.channel().remoteAddress().toString();
            LeeCoMessage loginResp = null;
            // 重复登陆 则拒绝
            if (nodeCheck.containsKey(nodeIndex)){
                loginResp = buildLoginResp((byte) -1);
            }else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOk = false;
                for (String wip : whiteList) {
                    if (wip.equals(ip)) {
                        isOk = true;
                        break;
                    }
                }
                loginResp = isOk ? buildLoginResp((byte) 0) : buildLoginResp((byte) -1);
                if (isOk) {
                    nodeCheck.put(nodeIndex, true);
                }
                LeeCoRpcApplication.logger.info("the login response is : " + loginResp + " body [ " + loginResp.getBody() + " ]");
            }
            ctx.writeAndFlush(loginResp);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.fireExceptionCaught(cause);
    }
}
