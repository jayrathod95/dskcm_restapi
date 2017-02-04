package deskcomm_restapi.exposed.user;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.support.ErrorCode;
import deskcomm_restapi.support.ErrorType;
import deskcomm_restapi.support.IncorrectPasswordResponse;
import deskcomm_restapi.support.Response1;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 19-01-2017.
 */
@Path("/user/{" + Keys.USER_UUID + "}/change_email")
public class ChangeEmail {
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String handlePost(@PathParam(Keys.USER_UUID) String uuid, @FormParam(Keys.USER_PASSWORD) String password, @FormParam(Keys.USER_EMAIL) String newEmail) {
        try {
            User user = new User(uuid);
            boolean b = user.updateEmail(uuid, password, newEmail);
            if (b)
                return new Response1<JSONObject>(true, ErrorType.ERROR_NONE, ErrorCode.NONE, "Email Changed Successfully. Please confirm email by clicking on the link provided Otherwise the action will reverted.", null).toString();
            else return new IncorrectPasswordResponse().toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response1<JSONObject>(false, ErrorType.ERROR_KNOWN, ErrorCode.SQL, e.getMessage(), null).toString();
        }
    }
}
