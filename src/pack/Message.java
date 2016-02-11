package pack;

import java.util.Date;

/**
 * Created by ASUS on 11.02.2016.
 */
public class Message {
    private String id;
    private String author;
    private Date timestamp;
    private String message;

    public Message(String author, Date timestamp, String message, String id) {
        this.author = author;
        this.timestamp = timestamp;
        this.message = message;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return  "id : " + id + " | " + author + " | " + timestamp + ": " + message;
    }
}
