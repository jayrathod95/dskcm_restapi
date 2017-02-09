package deskcomm_restapi.core;

/**
 * Created by Jay Rathod on 06-01-2017.
 */
public class Event {
    private String content;

    public Event(String s) {
        content = s;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

