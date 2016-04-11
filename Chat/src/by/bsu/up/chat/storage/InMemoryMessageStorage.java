package by.bsu.up.chat.storage;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.json.*;


public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();


    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public void addMessage(Message message)  {
        messages.add(message);
        try{
        saveMessages(messages);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean updateMessage(Message message) {
        for (int i = 0; i<messages.size();i++){
            if (messages.get(i).getId().equals(message.getId())){
                messages.get(i).setText(message.getText());
                messages.get(i).setIsEdit("was edited");
                try {
                    saveMessages(messages);
                }catch (IOException e){
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        for (int i = 0; i<messages.size(); i++){
            if (messages.get(i).getId().equals(messageId)){
                messages.get(i).setText("DELETED");
                try {
                    saveMessages(messages);
                }catch (IOException e){
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return messages.size();
    }

    @Override
    public  void loadMessages(){
        try {
            JsonArray forArray = getJson();
            if (!forArray.isEmpty()){
                JsonArray array = forArray.getJsonArray(0);
                messages.clear();
                for (int i = 0; i < array.size(); i++) {
                    JsonObject tmpObject = array.getJsonObject(i);
                    Message tempMessage = new Message(tmpObject);
                    messages.add(tempMessage);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public static JsonArray getJson() throws IOException {
        List<String> list= Files.readAllLines(Paths.get("history.json"));
        String JSONData = list.toString();
        JsonReader forRead = Json.createReader(new StringReader(JSONData));
        JsonArray forArray = forRead.readArray();
        forRead.close();
        return forArray;
    }

    @Override
    public void saveMessages(List<Message> messages ) throws  IOException{
        if (!messages.isEmpty()) {
            FileWriter out = new FileWriter("history.json");
            JsonWriter writeHistory = Json.createWriter(out);
            JsonArrayBuilder jsonHistory = Json.createArrayBuilder();
            for (Message i : messages) {
                jsonHistory.add(toJson(i));
            }
            JsonArray arr = jsonHistory.build();
            writeHistory.writeArray(arr);
            out.close();
            writeHistory.close();
        }
    }


    public static JsonObject toJson(Message aHistory) {
        return Json.createObjectBuilder().add("id", aHistory.getId())
                .add("author", aHistory.getAuthor())
                .add("timestamp", aHistory.getTimestamp())
                .add("message", aHistory.getText())
                .add("isEdit",aHistory.getIsEdit()).build();

    }
}