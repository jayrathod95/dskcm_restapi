package deskcomm_restapi.core;

/**
 * Created by Jay Rathod on 07-01-2017.
 */
public class Keys {
    final public static String USER_FIRSTNAME = "fname";
    final public static String USER_LASTNAME = "lname";
    final public static String USER_EMAIL = "email";
    final public static String USER_MOBILE = "mobile";
    final public static String USER_PASSWORD = "password";
    final public static String USER_REP_PASSWORD = "rpassword";
    final public static String USER_IMG_URL = "img_url";
    final public static String USER_CREATED = "created";
    final public static String USER_UID = "_uid";
    public static final int ERROR_NONE = 0;
    public static final int ERROR_KNOWN = 1;
    public static final int ERROR_UNKNOWN = 2;


    public static final String JSON_RESULT = "Result";
    public static final String JSON_ERROR_TYPE = "ErrorType";
    public static final String JSON_MESSAGE = "Message";
    public static final String PARAM_USERNAME = "email";
    public static final String PARAM_PASSWORD = "password";
    public static final String USER_UUID = "_uuid";
    public static final String GENDER = "gender";
    public static final String SESSION_ID = "session_id";
    public static final String ERROR_CODE = "error_code";
    public static final String JSON_DATA = "data";

    public class JSON {
        public static final String RESULT = "Result";
        public static final String JSON_ERROR_TYPE = "ErrorType";
        public static final String JSON_MESSAGE = "Message";
        public static final String DATA = "data";
        public static final String SESSION_ID = "session_id";
        public static final String ERROR_ID = "error_id";
        public static final String JSON_ERROR_ID = "error_id";
    }

    public class PARAM {
        public static final String SESSION_ID = "session_id";
        public static final String UUID_USER = "_uuid";
    }
}
