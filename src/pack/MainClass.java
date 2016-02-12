package pack;

import jdk.nashorn.api.scripting.JSObject;

import javax.json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        System.out.println("3.Show the history");
        System.out.println("4.Delete message");
        System.out.println("5.Find message by author");
        System.out.println("6.Find message by word");
        System.out.println("7.FInd by regular expression");
        System.out.println("8.Find by time period");
        System.out.println("9.End of program");
        while (!choice.equals("9"))
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
                        JsonArrayBuilder wrightArray = Json.createArrayBuilder();
                        for (int i = 0;i<hist.size();i++)
                        {
                            wrightArray.add(Json.createObjectBuilder().add("id",hist.get(i).getId())
                                    .add("author",hist.get(i).getAuthor())
                                    .add("timestamp",hist.get(i).getTimestamp().getTime())
                                    .add("message", hist.get(i).getMessage()).build());
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
                    System.out.println("input author");
                    Scanner sc = new Scanner(System.in);
                    String auth = sc.nextLine();
                    boolean ifFind = false;
                    for (Message iter: hist)
                    {
                        if (iter.getAuthor().equals(auth))
                        {
                            System.out.println(iter.toString());
                            ifFind = true;
                        }
                    }
                    if (ifFind)
                        System.out.println("Successfully done");
                    else System.out.println("No message from this author");
                    break;
                }
                case "6":{
                    System.out.println("input word");
                    Scanner sc = new Scanner(System.in);
                    String word = sc.next();
                    boolean ifFind = false;
                    for (Message it: hist)
                    {
                        if (it.getMessage().contains(word))
                        {
                            System.out.println(it.toString());
                            ifFind = true;
                        }
                    }
                    if (ifFind)
                        System.out.println("Successfully done");
                    else System.out.println("No message with this word");
                    break;
                }
                case "8":{
                    Scanner sc = new Scanner(System.in);
                    boolean ifFind = false;
                    System.out.println("input start time in format: MM/dd/yyyy HH:mm:ss");
                    SimpleDateFormat start = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date stDat=null;
                    try {
                        stDat = start.parse(sc.nextLine());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println("input end time in format: MM/dd/yyyy HH:mm:ss");
                    SimpleDateFormat end = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date enDat=null;
                    try {
                        enDat = end.parse(sc.nextLine());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    for (Message iter: hist)
                    {
                        if (iter.getTimestamp().after(stDat) && iter.getTimestamp().before(enDat))
                        {
                            System.out.println(iter.toString());
                            ifFind = true;
                        }
                    }
                    if (ifFind)
                        System.out.println("Successfully done");
                    else System.out.println("No message of this period");
                    break;
                }
                case "7":{
                    Scanner sc = new Scanner(System.in);
                    boolean ifFind = false;
                    System.out.println("input regular expression");
                    String regEx = sc.nextLine();
                    Pattern pat = Pattern.compile(regEx);
                    for (Message iter: hist)
                    {
                        Matcher matcher = pat.matcher(iter.getMessage());
                        if (matcher.find())
                        {
                            ifFind = true;
                            System.out.println(iter.toString());
                        }
                    }
                    if (!ifFind)
                        System.out.println("There are no messages with this regular expression");
                    break;
                }
                case "9":{
                    System.out.println("End of program");
                    break;
                }
                default:{
                    System.out.println("You need number from 0 to 9");
                    break;
                }
            }
        }
        in.close();
    }
}
