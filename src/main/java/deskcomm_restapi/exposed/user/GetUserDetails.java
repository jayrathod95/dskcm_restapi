package deskcomm_restapi.exposed.user;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.support.*;
import org.json.JSONObject;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 23-01-2017.
 */
@Path("/user/{" + Keys.USER_UUID + "}/get_info")
public class GetUserDetails {
    @POST
    public String getUserDetails(@PathParam(Keys.USER_UUID) String resourceUuid, @FormParam(Keys.USER_UUID) String uuid, @FormParam(Keys.JSON.SESSION_ID) String sessionId) {
        try {
            if (User.verifySession(uuid, sessionId)) {

                try {
                    User user = new User(resourceUuid);
                    if (user.fetch()) {
                        return new Response1<>(true, ErrorType.ERROR_NONE, ErrorCode.NONE, ErrorMessage.NONE, user.toJSONObject()).toString();
                    } else {
                        return new Response1<JSONObject>(false, ErrorType.ERROR_UNKNOWN, ErrorCode.UNKNOWN, ErrorMessage.UNKNOWN, null).toString();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new Response1<JSONObject>(false, ErrorType.ERROR_UNKNOWN, ErrorCode.UNKNOWN, ErrorMessage.UNKNOWN, null).toString();
                }
            } else {
                return new SessionVerificationFailedResponse().toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new SQLExceptionResponse(e.getMessage()).toString();
        }
    }
}
