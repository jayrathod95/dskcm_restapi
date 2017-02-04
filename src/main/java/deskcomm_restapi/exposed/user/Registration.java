package deskcomm_restapi.exposed.user;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.core.UserBuilder;
import deskcomm_restapi.exceptions.InsufficientParamException;
import deskcomm_restapi.exceptions.InvalidParamException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 06-01-2017.
 */
@Path("/user/register")
public class Registration {



    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String register(@FormParam(Keys.USER_FIRSTNAME) String fname, @FormParam(Keys.USER_LASTNAME) String lname, @FormParam(Keys.USER_EMAIL) String email, @FormParam(Keys.USER_PASSWORD) String password, @FormParam(Keys.USER_UID) String uid, @FormParam(Keys.USER_IMG_URL) String imgUrl, @FormParam("mobile") String mobile) throws InsufficientParamException {


        try {
            UserBuilder builder = User.getUserBuilder();
            builder.setFirstname(fname);
            builder.setLastname(lname);
            builder.setEmail(email);
            builder.setPassword(password);
            builder.setUid(uid);
            builder.setMobile(mobile);
            builder.setImage_url(imgUrl);
            if (builder.build()) {
                JSONObject jsonObject = new JSONObject();
                return jsonObject.accumulate(Keys.JSON_RESULT, true).accumulate(Keys.JSON_ERROR_TYPE, Keys.ERROR_NONE).toString();
            } else {
                return new JSONObject().accumulate(Keys.JSON_RESULT, false).accumulate(Keys.JSON_ERROR_TYPE, Keys.ERROR_UNKNOWN).toString();
            }
        } catch (InvalidParamException e) {
            e.printStackTrace();
            JSONObject jsonObject = new JSONObject().accumulate(Keys.JSON_RESULT, false).accumulate(Keys.JSON_ERROR_TYPE, Keys.ERROR_KNOWN).accumulate(Keys.JSON_MESSAGE, e.getMessage());
            return jsonObject.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            String message = e.getMessage();
            if (e.getMessage().contains("Duplicate")) {
                if (e.getMessage().contains("email"))
                    message = "An account with provided email already exists";
                else if (e.getMessage().contains("mobile"))
                    message = "An account with provided mobile already exists";
                else if (e.getMessage().contains("uid"))
                    message = "An account with provided EID already exists";
            }
            return new JSONObject().accumulate(Keys.JSON_RESULT, true).accumulate(Keys.JSON_ERROR_TYPE, Keys.ERROR_KNOWN).accumulate(Keys.JSON_MESSAGE, message).toString();
        }
        //     JsonObjectBuilder builder = Json.createObjectBuilder().add(Keys.JSON_RESULT, false).add(Keys.JSON_ERROR_TYPE, Keys.ERROR_UNKNOWN);
        //    return builder.build();

    }

}
