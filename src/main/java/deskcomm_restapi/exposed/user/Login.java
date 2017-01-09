package deskcomm_restapi.exposed.user;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.exceptions.LoginException;
import deskcomm_restapi.support.Response;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 09-01-2017.
 */
@Path("/user/login")
@Produces(MediaType.APPLICATION_JSON)
public class Login {
    @POST
    public String login(@FormParam(Keys.PARAM_USERNAME) String username, @FormParam(Keys.PARAM_PASSWORD) String password) {

        if (username.length() > 0 && password.length() > 0) {
            try {
                User user = User.login(username, password);
                return user.toString();
            } catch (SQLException e) {
                e.printStackTrace();
                return new Response(false, Keys.ERROR_KNOWN, e.getMessage()).toString();
            } catch (LoginException e) {
                e.printStackTrace();
                return new Response(false, Keys.ERROR_KNOWN, e.getMessage()).toString();
            }
        } else {
            return new Response(false, Keys.ERROR_KNOWN, "Username/Password provided is empty").toString();
        }
    }
}
