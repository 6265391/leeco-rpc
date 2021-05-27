package com.leeco.rpc.framework.server;

import com.leeco.rpc.framework.core.NettyConstant;
import com.leeco.rpc.framework.handler.HeartBeatRespHandler;
import com.leeco.rpc.framework.handler.LoginAuthRespHandler;
import com.leeco.rpc.framework.serializable.marshalling.decode.LeeCoMessageDecode;
import com.leeco.rpc.framework.serializable.marshalling.encode.LeeCoMessageEncode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 客户端
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 18:53
 */
public class LeeCoRpcServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void bind() throws Exception {
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    /*  第一个参数：1024*1024: 最大长度
                                        第二个参数: 从第4个bytes开始表示是长度
                                        第三个参数: 有4个bytes的长度表示是长度  */
                                    .addLast(new LeeCoMessageDecode(1024 * 1024, 4 , 4))
                                    .addLast(new LeeCoMessageEncode())
                                    .addLast(new ReadTimeoutHandler(50))
                                    .addLast(new LoginAuthRespHandler())
                                    .addLast(new HeartBeatRespHandler());
                        }
                    });
            ChannelFuture sync = bootstrap.bind(NettyConstant.REMOTE_IP, NettyConstant.PORT).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new LeeCoRpcServer().bind();
    }

}
