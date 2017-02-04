package deskcomm_restapi.exposed.user;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.exceptions.OperationFailedException;
import deskcomm_restapi.support.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;

import javax.inject.Singleton;
import javax.ws.rs.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import static deskcomm_restapi.core.Keys.*;
import static deskcomm_restapi.support.FileUtils.isImageFile;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * Created by Jay Rathod on 31-01-2017.
 */
@Singleton
@Path("/user/{" + USER_UUID + "}/profile_pic")
public class ProfilePicture {

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(TEXT_PLAIN)
    public String processForm(@PathParam(USER_UUID) String uuid, @FormDataParam(Keys.SESSION_ID) String sessionId, @FormDataParam("xml") InputStream is, @FormDataParam("xml") FormDataContentDisposition fileDetails) throws InterruptedException {
        String fileName = fileDetails.getFileName();
        if (isImageFile(fileName)) {
            try {
                if (User.verifySession(uuid, sessionId)) {
                    String fileExtension = FilenameUtils.getExtension(fileName);
                    File file = new File(FilenameUtils.normalize(uuid + "." + fileExtension));
                    try {
                        FileUtils.copyInputStreamToFile(is, file);
                        User user = new User(uuid, sessionId);
                        user.updateImageURL(file.getName());
                        return new Response1<JSONObject>(true, ERROR_NONE, null, "Image uploaded successfully", null).toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new Response1<JSONObject>(false, ERROR_UNKNOWN, ErrorCode.UNKNOWN, ErrorMessage.UNKNOWN, null).toString();
                    } catch (OperationFailedException e) {
                        e.printStackTrace();
                        return new Response1<JSONObject>(false, ErrorType.ERROR_KNOWN, ErrorCode.DEFAULT, e.getMessage(), null).toString();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return new Response1<JSONObject>(false, ErrorType.ERROR_KNOWN, ErrorCode.SQL, ErrorMessage.UNKNOWN, null).toString();
                    }
                } else {
                    return new Response1<JSONObject>(false, ErrorType.ERROR_KNOWN, ErrorCode.SESSION_VER_FAIL, ErrorMessage.SESSION_VER_FAIL, null).toString();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new SQLExceptionResponse(e.getMessage()).toString();
            }
        } else {
            return new Response1<JSONObject>(false, ErrorType.ERROR_KNOWN, ErrorCode.UNSUPPORTED_FILE_FORMAT, ErrorMessage.UNSUPPORTED_FILE_FORMAT, null).toString();
        }
    }

}
