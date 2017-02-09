package deskcomm_restapi.core;

import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class TextMessage<S, R> {
    private S sender;
    private R receiver;
    private String body;
    private Timestamp timestamp;


    public S getSender() {
        return sender;
    }

    public void setSender(S sender) {
        this.sender = sender;
    }

    public R getReceiver() {
        return receiver;
    }

    public void setReceiver(R receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new JSONObject().put("sender", sender).put("receiver", receiver).put("body", body).put("timestamp", timestamp.toString()).toString();
    }
}
