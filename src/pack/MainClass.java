package pack;

import jdk.nashorn.api.scripting.JSObject;

import javax.json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by ASUS on 11.02.2016.
 */
public class MainClass {
    public static void main(String[] args)throws IOException{
        ArrayList<Message> hist = new ArrayList<Message>();
        String choice="";
        Scanner in=new Scanner(System.in);
        System.out.println("Choose your variant");
        System.out.println("0.Load messages from file");
        System.out.println("1.Save messages in file");
        System.out.println("2.Add a message");
        System.out.println("3.Look the history");
        System.out.println("4.Delete message");
        System.out.println("5.Log off");
        while (!choice.equals("5"))
        {
            choice=in.next();
            switch(choice){
                case "0":{
                    String JSONData= Files.readAllLines(Paths.get("history.json")).toString();
                    JsonReader forRead = Json.createReader(new StringReader(JSONData));
                    JsonArray forArray = forRead.readArray();
                    if(forArray.size() == 0)
                    {
                        System.out.println("Your history is empty");
                        break;
                    }
                    JsonArray mas = forArray.getJsonArray(0);
                    forRead.close();
                    for (int i = 0;i<mas.size();i++)
                    {
                        JsonObject tmp = mas.getJsonObject(i);
                        Date tmpTime = new Date(tmp.getJsonNumber("timestamp").longValue());
                        Message tempMes = new Message(tmp.getString("author"),tmpTime,
                                tmp.getString("message"),tmp.getString("id"));
                        hist.add(tempMes);
                    }
                    System.out.println("Successfully done");
                    break;
                }
                case "3":{
                    if (!hist.isEmpty()){
                        for (Message i: hist)
                        {
                            System.out.println(i.toString());
                        }
                        break;
                    }
                    else {
                        System.out.println("empty history");
                        break;
                    }
                }
                case "4":{
                    System.out.println("Input need id");
                    Scanner sc = new Scanner(System.in);
                    String idNeed = sc.next();
                    int count = hist.size();
                    for (int i = 0;i<hist.size();i++)
                    {
                        if (hist.get(i).getId().equals(idNeed)){
                            hist.remove(i);
                            System.out.println("Successfully done");
                        }
                    }
                    if (count == hist.size())
                        System.out.println("there is no such message");
                    break;
                }
                case "2":{
                    Scanner sc = new Scanner(System.in);
                    System.out.println("input your name");
                    String name = sc.nextLine();
                    System.out.println("input your message");
                    String mes = sc.nextLine();
                    Date tmpDate = new Date();
                    Message temp = new Message(name,tmpDate,mes,"random-id-"+((hist.size()+1)));
                    hist.add(temp);
                    System.out.println("Successfully added");
                    break;
                }
                case "1":{
                    if (!hist.isEmpty()){
                        FileWriter forOut = new FileWriter("history.json");
                        JsonWriter forWrite = Json.createWriter(forOut);
                        // JsonObject t[]= new JsonObject[hist.size()];
                        JsonArrayBuilder wrightArray = Json.createArrayBuilder();
                        for (int i = 0;i<hist.size();i++)
                        {
                            wrightArray.add(Json.createObjectBuilder().add("id",hist.get(i).getId())
                                    .add("author",hist.get(i).getAuthor())
                                    .add("timestamp",hist.get(i).getTimestamp().getTime())
                                    .add("message", hist.get(i).getMessage()).build());
                            // wrightArray.add(t[i]);
                        }
                        JsonArray arr = wrightArray.build();
                        forWrite.writeArray(arr);
                        forWrite.close();
                        System.out.println("Successfully done");
                        break;
                    }
                    else {
                        System.out.println("Firstly download message history");
                        break;
                    }
                }
                case "5":{
                    System.out.println("End of program");
                    break;
                }
                default:{
                    System.out.println("You need number from 0 to 5");
                    break;
                }
            }
        }
        in.close();
    }
}
