package pack.models;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 01.05.2016.
 */
public class LoginStorage {
    private ArrayList<Account> accounts = new ArrayList<Account>();

    public  void loadAccounts(){
        try {
            JsonArray forArray = getJson();
            if (!forArray.isEmpty()){
                JsonArray array = forArray.getJsonArray(0);
                accounts.clear();
                for (int i = 0; i < array.size(); i++) {
                    JsonObject tmpObject = array.getJsonObject(i);
                    Account temp = new Account(tmpObject);
                    accounts.add(temp);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static JsonArray getJson() throws IOException {
        Path s = Paths.get("users.json");
        List<String> list= Files.readAllLines(s);
        String JSONData = list.toString();
        JsonReader forRead = Json.createReader(new StringReader(JSONData));
        JsonArray forArray = forRead.readArray();
        forRead.close();
        return forArray;
    }
    public boolean findAccount(String login, String pass_hash){
        for (Account account : accounts) {
            if (account.getLogin().equals(login) && account.getPass_hash().equals(pass_hash)) {
                return true;
            }
        }
        return false;
    }
}
