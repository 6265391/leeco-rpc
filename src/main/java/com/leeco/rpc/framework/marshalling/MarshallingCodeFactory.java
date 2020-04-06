package com.leeco.rpc.framework.marshalling;

import org.jboss.marshalling.*;

import java.io.IOException;

/**
 * MarshallingCodeFactory
 * @author liuqiang@ourdocker.cn
 * @version 0.0.1
 * @date 2020/4/6 17:15
 */

public class MarshallingCodeFactory {

    public static Unmarshaller buildMarshallingDecoder() throws IOException {
        final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        return factory.createUnmarshaller(configuration);
    }

    public static Marshaller buildMarshallingEncoder() throws IOException {
        final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        return factory.createMarshaller(configuration);
    }
}
