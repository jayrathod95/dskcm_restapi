package deskcomm_restapi.exposed.user;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.support.*;
import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 22-01-2017.
 */
@Path("/users/get_all")
public class GetAllUsers {
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllUsers(@FormParam(Keys.PARAM.UUID_USER) String uuid, @FormParam(Keys.PARAM.SESSION_ID) String sessionId) {
        
        try {
            if (User.verifySession(uuid, sessionId)) {
                try {
                    JSONArray users = User.getAllUsers();
                    return new Response1<JSONArray>(true, ErrorType.ERROR_NONE, ErrorCode.NONE, ErrorMessage.NONE, users).toString();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new SQLExceptionResponse(e.getMessage()).toString();
                }
            } else {
                return new SessionVerificationFailedResponse().getMessage();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new SQLExceptionResponse(e.getMessage()).toString();
        }
    }
}
