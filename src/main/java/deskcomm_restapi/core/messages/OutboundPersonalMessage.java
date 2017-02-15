package deskcomm_restapi.core.messages;

import org.json.JSONObject;

import static deskcomm_restapi.core.Keys.*;

/**
 * Created by jay_rathod on 15-02-2017.
 */
public class OutboundPersonalMessage {

    private String uuid;
    private String toUuid;
    private String fromUuid;
    private String data;
    private String timeStamp;

    public OutboundPersonalMessage(String uuid, String toUuid, String fromUuid, String data, String timeStamp) {
        this.uuid = uuid;
        this.toUuid = toUuid;
        this.fromUuid = fromUuid;
        this.data = data;
        this.timeStamp = timeStamp;
    }

    public OutboundPersonalMessage() {

    }

    @Override
    public String toString() {
        return this.toJSON().toString();
    }

    public JSONObject toJSON() {
        return new JSONObject()
                .put(MESSAGE_ID, uuid).put(MESSAGE_TO, toUuid)
                .put(MESSAGE_FROM, fromUuid)
                .put(MESSAGE_BODY, data)
                .put(SERVER_TIMESTAMP, timeStamp);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFromUuid() {
        return fromUuid;
    }

    public void setFromUuid(String fromUuid) {
        this.fromUuid = fromUuid;
    }

    public String getToUuid() {
        return toUuid;
    }

    public void setToUuid(String toUuid) {
        this.toUuid = toUuid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
