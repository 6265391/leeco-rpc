package com.leeco.rpc.framework.marshalling.decode;

import com.leeco.rpc.framework.message.Header;
import com.leeco.rpc.framework.message.LeeCoMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义消息解码器
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 17:38
 */
public class LeeCoMessageDecode extends LengthFieldBasedFrameDecoder {

    MarshallingDecoder marshallingDecoder;

    public LeeCoMessageDecode(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        LeeCoMessage message = new LeeCoMessage();
        Header header = new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionId(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());

        int size = in.readInt();
        if (size > 0){
            Map<String,Object> attach = new ConcurrentHashMap<>(size);
            int keySize = 0;
            byte[] keyArrays = null;
            String key = null;
            for(int i = 0; i < size; i++){
                keySize = in.readInt();
                keyArrays = new byte[keySize];
                key = new String(keyArrays, StandardCharsets.UTF_8);
                attach.put(key,marshallingDecoder.decode(in));
            }
            header.setAttachment(attach);
        }
        if (in.readableBytes() > 4){
            message.setBody(marshallingDecoder.decode(in));
        }
        message.setHeader(header);
        return message;
    }
}
