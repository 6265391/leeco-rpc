package com.leeco.rpc.framework.marshalling.encode;

import com.leeco.rpc.framework.message.LeeCoMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 自定义消息编码器
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 16:48
 */
public class LeeCoMessageEncode extends MessageToByteEncoder<LeeCoMessage> {

    private MarshallingEncoder marshallingEncoder;

    public LeeCoMessageEncode() throws IOException {
        marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LeeCoMessage msg, ByteBuf sendBuf) throws Exception {
        if (msg == null || msg.getHeader() == null){
            throw new Exception(">>>>> the encode message is null <<<<<");
        }
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionId());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeInt(msg.getHeader().getAttachment().size());
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String,Object> param : msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes(StandardCharsets.UTF_8);
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value,sendBuf);
        }
        if (msg.getBody() != null){
            marshallingEncoder.encode(msg.getBody(),sendBuf);
        }else{
            // 之前写了crcCode 4bytes，除去crcCode和length 8bytes即为更新之后的字节
            sendBuf.setInt(4,sendBuf.readableBytes());
        }
    }

}
