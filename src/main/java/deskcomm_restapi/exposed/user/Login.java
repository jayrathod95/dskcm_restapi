package deskcomm_restapi.exposed.user;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.exceptions.LoginException;
import deskcomm_restapi.support.ErrorCode;
import deskcomm_restapi.support.ErrorMessage;
import deskcomm_restapi.support.ErrorType;
import deskcomm_restapi.support.Response1;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;


/**
 * Created by Jay Rathod on 09-01-2017.
 */
@Path("/user/login")
public class Login {
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@FormParam(Keys.PARAM_USERNAME) String username, @FormParam(Keys.PARAM_PASSWORD) String password) {

        if (username.length() > 0 && password.length() > 0) {
            try {
                User user = User.login(username, password);
                return new Response1<JSONObject>(true, ErrorType.ERROR_NONE, ErrorCode.NONE, ErrorMessage.NONE, user.toJSONObject()).toString();
            } catch (SQLException e) {
                e.printStackTrace();
                return new Response1<JSONObject>(false, ErrorType.ERROR_KNOWN, ErrorCode.SQL, e.getMessage(), null).toString();
            } catch (LoginException e) {
                e.printStackTrace();
                return new Response1<JSONObject>(false, ErrorType.ERROR_KNOWN, ErrorCode.INCORRECT_USERNAME_PASSWORD, e.getMessage(), null).toString();
            }
        } else {
            return new Response1<JSONObject>(false, Keys.ERROR_KNOWN, ErrorCode.DEFAULT, "Username/Password provided is empty", null).toString();
        }
    }
}
