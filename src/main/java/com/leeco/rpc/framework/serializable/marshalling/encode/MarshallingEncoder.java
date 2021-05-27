package com.leeco.rpc.framework.serializable.marshalling.encode;

import com.leeco.rpc.framework.serializable.marshalling.ChannelBufferByteOutput;
import com.leeco.rpc.framework.serializable.marshalling.MarshallingCodeFactory;
import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

/**
 * 消息编码工具类 jboss.marshalling
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 17:07
 */
public class MarshallingEncoder {

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    private final Marshaller marshaller;

    MarshallingEncoder() throws IOException {
        marshaller = MarshallingCodeFactory.buildMarshallingEncoder();
    }

    protected void encode(Object msg, ByteBuf out) throws IOException {
        try{
            int lengthPos = out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
            marshaller.start(output);
            marshaller.writeObject(msg);
            marshaller.finish();
            out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            marshaller.close();
        }
    }

}
