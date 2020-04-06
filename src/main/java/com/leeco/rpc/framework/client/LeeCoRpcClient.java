package com.leeco.rpc.framework.client;

import com.leeco.rpc.framework.core.NettyConstant;
import com.leeco.rpc.framework.handler.HeartBeatReqHandler;
import com.leeco.rpc.framework.handler.LoginAuthReqHandler;
import com.leeco.rpc.framework.marshalling.decode.LeeCoMessageDecode;
import com.leeco.rpc.framework.marshalling.encode.LeeCoMessageEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 客户端
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 18:53
 */
public class LeeCoRpcClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port) throws Exception {
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    /* 第一个参数：1024*1024: 最大长度
                                       第二个参数: 从第4个bytes开始表示是长度
                                       第三个参数: 有4个bytes的长度表示是长度  */
                                    .addLast(new LeeCoMessageDecode(1024 * 1024, 4 , 4))
                                    .addLast(new LeeCoMessageEncode())
                                    .addLast(new ReadTimeoutHandler(50))
                                    .addLast(new LoginAuthReqHandler())
                                    .addLast(new HeartBeatReqHandler());
                        }
                    });
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress(host,port),new InetSocketAddress(NettyConstant.LOCAL_IP,NettyConstant.LOCAL_PORT)).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            // 释放资源hi后 清空资源 再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        TimeUnit.SECONDS.sleep(5);
                        connect(NettyConstant.REMOTE_IP,NettyConstant.PORT);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new LeeCoRpcClient().connect(NettyConstant.REMOTE_IP,NettyConstant.PORT);
    }

}
