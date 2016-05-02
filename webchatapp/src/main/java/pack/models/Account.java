package pack.models;

import javax.json.JsonObject;

/**
 * Created by ASUS on 01.05.2016.
 */
public class Account {
    private String login;
    private String pass_hash;

    public Account() {
    }

    public Account(String login, String pass_hash) {
        this.login = login;
        this.pass_hash = pass_hash;
    }

    public Account(JsonObject obj){
        this.login = obj.getString("login");
        this.pass_hash = obj.getString("pass-hash");
    }

    public String getPass_hash() {
        return pass_hash;
    }

    public void setPass_hash(String pass_hash) {
        this.pass_hash = pass_hash;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
