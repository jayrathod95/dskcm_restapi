package deskcomm_restapi.exposed.user;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.core.UserBuilder;
import deskcomm_restapi.exceptions.InsufficientParamException;
import deskcomm_restapi.exceptions.InvalidParamException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 06-01-2017.
 */
@Path("user/register")
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
            if (builder.build()) {
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                return jsonObjectBuilder.add(Keys.JSON_RESULT, true).add(Keys.JSON_ERROR_TYPE, Keys.ERROR_NONE).build().toString();
            } else {
                return Json.createObjectBuilder().add(Keys.JSON_RESULT, false).add(Keys.JSON_ERROR_TYPE, Keys.ERROR_UNKNOWN).build().toString();
            }
        } catch (InvalidParamException e) {
            e.printStackTrace();
            JsonObjectBuilder builder = Json.createObjectBuilder().add(Keys.JSON_RESULT, false).add(Keys.JSON_ERROR_TYPE, Keys.ERROR_KNOWN).add(Keys.JSON_MESSAGE, e.getMessage());
            return builder.build().toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return Json.createObjectBuilder().add(Keys.JSON_RESULT, true).add(Keys.JSON_ERROR_TYPE, Keys.ERROR_KNOWN).add(Keys.JSON_MESSAGE, e.getMessage()).build().toString();
        }
        //     JsonObjectBuilder builder = Json.createObjectBuilder().add(Keys.JSON_RESULT, false).add(Keys.JSON_ERROR_TYPE, Keys.ERROR_UNKNOWN);
        //    return builder.build();

    }

}
