package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.core.User;
import org.junit.Test;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class WebSocketServerEndpointTest {
    @Test
    public void getSessionById() throws Exception {

    }

    @Test
    public void onOpen() throws Exception {

    }

    @Test
    public void onMessage() throws Exception {

    }

    @Test
    public void processMessageAcknowlegent() throws Exception {

    }

    @Test
    public void onMessage1() throws Exception {

    }

    @Test
    public void getGroupCreationSuccessfulResponse() throws Exception {

    }

    @Test
    public void getGroupCreationFailedResponse() throws Exception {

    }

    @Test
    public void onClose() throws Exception {
        User.updateStatusAsOffline("81a8b89e-cff0-41e9-b6b6-3e57d493012a");
    }

    @Test
    public void storeMessage() throws Exception {
        // WebSocketServerEndpoint socketServerEndpoint = new WebSocketServerEndpoint();


    }

}