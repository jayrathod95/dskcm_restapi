package deskcomm_restapi.exposed.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class FileDecoder implements Decoder.Binary<InputStream> {
    @Override
    public InputStream decode(ByteBuffer byteBuffer) throws DecodeException {
        return null;
    }

    @Override
    public boolean willDecode(ByteBuffer byteBuffer) {
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
