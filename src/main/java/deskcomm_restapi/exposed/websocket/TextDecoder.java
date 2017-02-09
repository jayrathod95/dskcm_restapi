package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.support.WebSocketPacket;

import javax.websocket.EndpointConfig;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class TextDecoder implements javax.websocket.Decoder.Text<WebSocketPacket> {

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public WebSocketPacket decode(String s) {
        return new WebSocketPacket(s);
    }

    @Override
    public boolean willDecode(String s) {
        return false;
    }
}
