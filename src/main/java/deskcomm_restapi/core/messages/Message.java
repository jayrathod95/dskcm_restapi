package deskcomm_restapi.core.messages;

import org.json.JSONObject;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public abstract class Message {
    protected String messageId;
    protected String fromUuid;
    protected String toUuid;
    protected String body;

    public Message(String messageId, String fromUuid, String toUuid, String body) {
        this.messageId = messageId;
        this.fromUuid = fromUuid;
        this.toUuid = toUuid;
        this.body = body;


    }

    abstract public boolean saveToDatabase();

    abstract public JSONObject toJSON();

    abstract public void send();


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
