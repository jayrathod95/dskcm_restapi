package deskcomm_restapi.exposed.user;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.support.ErrorCode;
import deskcomm_restapi.support.ErrorMessage;
import deskcomm_restapi.support.ErrorType;
import deskcomm_restapi.support.Response1;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 19-01-2017.
 */
@Path("/user/{" + Keys.USER_UUID + "}/change_password")
public class ChangePassword {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String changePassword(@PathParam(Keys.USER_UUID) String uuid, @FormParam("old_password") String oldPassword, @FormParam("new_password") String newPassword) {
        Response1<JSONObject> response1 = new Response1<>();
        try {
            User user = new User(uuid);
            boolean b = user.changePassword(oldPassword, newPassword);
            if (b) {
                response1.setResult(true);
                response1.setErrorType(Keys.ERROR_NONE);
                response1.setErrorCode(ErrorCode.NONE);
                response1.setMessage("Password changed successfully");
            } else {
                response1.setResult(false);
                response1.setErrorCode(ErrorCode.INCORRECT_PASSWORD);
                response1.setErrorType(ErrorType.ERROR_KNOWN);
                response1.setMessage(ErrorMessage.INCORRECT_PASSWORD);
            }
            return response1.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response1<JSONObject>(false, ErrorType.ERROR_KNOWN, ErrorCode.SQL, e.getMessage(), null).toString();
        }
    }
}
