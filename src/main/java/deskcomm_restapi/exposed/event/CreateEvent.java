package deskcomm_restapi.exposed.event;

import deskcomm_restapi.core.Event;
import deskcomm_restapi.core.Identity;
import deskcomm_restapi.exposed.websocket.WebSocketServerEndpoint;
import deskcomm_restapi.support.ErrorCode;
import deskcomm_restapi.support.ErrorType;
import deskcomm_restapi.support.Response1;
import deskcomm_restapi.support.SessionVerificationFailedResponse;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * Created by jay_rathod on 21-02-2017.
 */
@Path("/event/create")
public class CreateEvent {
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String handlePost(@FormParam("identity") String identityString, @FormParam("data") String data) {
        Identity identity = new Identity(new JSONObject(identityString));
        if (identity.verify()) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Event event = new Event(new JSONObject(data));
            event.setCreatedBy(identity.getUuid());
            try {
                if (event.saveToDatabase()) {
                    Response1<JSONObject> response1 = new Response1<>();
                    response1.setResult(true);
                    response1.setMessage("Event Created Successfully");
                    response1.setErrorType(ErrorType.ERROR_NONE);
                    response1.setErrorCode(ErrorCode.NONE);
                    //WebSocketServerEndpoint.pushEvent(event.getUuid());
                    WebSocketServerEndpoint.pushEvent(event.getUuid());
                    return response1.toString();
                } else {
                    Response1<JSONObject> response1 = new Response1<>();
                    response1.setResult(false);
                    response1.setMessage("Event Creation Failed");
                    response1.setErrorType(ErrorType.ERROR_UNKNOWN);
                    response1.setErrorCode(ErrorCode.NONE);
                    return response1.toString();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Response1<JSONObject> response1 = new Response1<>();
                response1.setResult(false);
                response1.setMessage("Event Creation Failed");
                response1.setErrorType(ErrorType.ERROR_KNOWN);
                response1.setErrorCode(ErrorCode.SQL);
                return response1.toString();
            }
        } else {
            return new SessionVerificationFailedResponse().getMessage();
        }
    }
}
