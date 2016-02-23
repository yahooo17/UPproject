package pack;

import javax.json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class History {
    private ArrayList<Message> history;

    public History() {
        history = new ArrayList<>();
    }

    public void loadMessages(JsonArray array) {
        history.clear();
        for (int i = 0; i < array.size(); i++) {
            JsonObject tmpObject = array.getJsonObject(i);
            Message tempMessage = new Message(tmpObject);
            history.add(tempMessage);
        }

    }

    public JsonArray createArray() {
        JsonArrayBuilder jsonHistory = Json.createArrayBuilder();
        for (Message message : history) {
            jsonHistory.add(toJson(message));
        }
        return jsonHistory.build();
    }

    public JsonObject toJson(Message aHistory) {
        return Json.createObjectBuilder().add("id", aHistory.getId())
                .add("author", aHistory.getAuthor())
                .add("timestamp", aHistory.getTimestamp().getTime())
                .add("message", aHistory.getMessage()).build();
    }

    public void saveMessages(JsonArray arr) throws IOException {
        FileWriter out = new FileWriter("history.json");
        JsonWriter writeHistory = Json.createWriter(out);
        writeHistory.writeArray(arr);
        out.close();
        writeHistory.close();
    }

    public void addMessage(Message temp) {
        history.add(temp);
    }

    public void showHistory() {
        if (!history.isEmpty()) {
            for (Message i : history) {
                System.out.println(i.toString());
            }
        } else {
            System.out.println("empty history");
        }
    }

    public int deleteMessage(String idNeed) {
        int count = 0;
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).getId().equals(idNeed)) {
                history.remove(i);
                count++;
            }
        }
        return count;
    }

    public int searchByAuthor(String author) {
        int count = 0;
        for (Message i : history) {
            if (i.getAuthor().equals(author)) {
                System.out.println(i.toString());
                count++;
            }
        }
        return count;
    }

    public int searchByWord(String word) {
        int count = 0;
        for (Message it : history) {
            if (it.getMessage().contains(word)) {
                System.out.println(it.toString());
                count++;
            }
        }
        return count;
    }

    public int searchByExpression(String expression) {
        int count = 0;
        Pattern pat = Pattern.compile(expression);
        for (Message it : history) {
            Matcher matcher = pat.matcher(it.getMessage());
            if (matcher.find()) {
                count++;
                System.out.println(it.toString());
            }
        }
        return count;
    }

    public int searchByTime(Date startDate, Date endDate) {
        int count = 0;
        for (Message it : history) {
            if (it.getTimestamp().after(startDate) && it.getTimestamp().before(endDate)) {
                System.out.println(it.toString());
                count++;
            }
        }
        return count;
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }
}
