package com.leeco.rpc.framework.marshalling.decode;

import com.leeco.rpc.framework.marshalling.ChannelBufferByteInput;
import com.leeco.rpc.framework.marshalling.MarshallingCodeFactory;
import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * 消息解码工具类 jboss.marshalling
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 17:40
 */
public class MarshallingDecoder {

    private final Unmarshaller unmarshaller;

    MarshallingDecoder() throws IOException {
        unmarshaller = MarshallingCodeFactory.buildMarshallingDecoder();
    }

    protected Object decode(ByteBuf in) throws Exception {
        int objectSize = in.readInt();
        ByteBuf buf = in.slice(in.readerIndex(), objectSize);
        ByteInput input = new ChannelBufferByteInput(buf);
        try{
            unmarshaller.start(input);
            Object obj = unmarshaller.readObject();
            unmarshaller.finish();
            in.readerIndex(in.readerIndex() + objectSize);
            return obj;
        }finally {
            unmarshaller.close();
        }
    }

}
