package deskcomm_restapi.support;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import deskcomm_restapi.core.Keys;
import org.json.JSONObject;

import static deskcomm_restapi.core.Keys.*;

/**
 * Created by Jay Rathod on 04-02-2017.
 */
public class Response1<T> {
    private boolean result;
    private int errorType;
    private String errorCode;
    private String message;
    private T data;

    public Response1() {
    }

    public Response1(@NotNull boolean result, @NotNull int errorType, @Nullable String errorCode, @Nullable String message, @Nullable T data) {
        this.result = result;
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put(JSON_RESULT, result)
                .put(JSON_ERROR_TYPE, errorType)
                .put(ERROR_CODE, errorCode)
                .put(JSON_MESSAGE, message)
                .put(Keys.JSON_DATA, data);
    }

    @Override
    public String toString() {
        return toJSONObject().toString();
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
