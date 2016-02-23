package pack;

import javax.json.JsonObject;
import java.util.Date;

public class Message {
    private String id;
    private String author;
    private Date timestamp;
    private String message;

    public Message(String id, String author, Date timestamp, String message) {
        this.author = author;
        this.timestamp = timestamp;
        this.message = message;
        this.id = id;
    }

    public Message(JsonObject temp) {
        this.author = temp.getString("author");
        this.timestamp = new Date(temp.getJsonNumber("timestamp").longValue());
        this.message = temp.getString("message");
        this.id = temp.getString("id");
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "id : " + id + " | " + author + " | " + timestamp + ": " + message;
    }
}
