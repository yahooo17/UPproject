package pack;


import javax.json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryWorker {
    final static String LOG_FILE_NAME = "logfile.txt";
    private ArrayList<Message> history;

    public HistoryWorker() throws IOException {
        history = new ArrayList<>();
        String choice = "";
        Scanner in = new Scanner(System.in);
        FileWriter clearFile = new FileWriter(LOG_FILE_NAME, false);
        clearFile.write("");
        FileWriter writeLog = new FileWriter(LOG_FILE_NAME, true);
        showVariants();
        while (!choice.equals("9")) {
            choice = in.next();
            switch (choice) {
                case "0": {
                    loadMessages(writeLog);
                    break;
                }
                case "1": {
                    saveMessages(writeLog);
                    break;
                }
                case "2": {
                    addMessage(writeLog);
                    break;
                }
                case "3": {
                    showHistory();
                    break;
                }
                case "4": {
                    deleteMessage(writeLog);
                    break;
                }
                case "5": {
                    searchByAuthor(writeLog);
                    break;
                }
                case "6": {
                    searchByWord(writeLog);
                    break;
                }
                case "7": {
                    searchByExpression(writeLog);
                    break;
                }
                case "8": {
                    searchByTime(writeLog);
                    break;
                }
                case "9": {
                    writeLog.write("End of program" + "\r\n");
                    System.out.println("End of program");
                    break;
                }
                default: {
                    System.out.println("You need number from 0 to 9");
                    break;
                }
            }
        }
        in.close();
        writeLog.close();
    }

    public void showVariants() {
        System.out.println("Choose your variant");
        System.out.println("0.Load messages from file");
        System.out.println("1.Save messages in file");
        System.out.println("2.Add a message");
        System.out.println("3.Show the history");
        System.out.println("4.Delete message");
        System.out.println("5.Search message by author");
        System.out.println("6.Search message by word");
        System.out.println("7.Search by regular expression");
        System.out.println("8.Search by time period");
        System.out.println("9.End of program");
    }

    public void loadMessages(FileWriter writeLog) throws IOException {
        writeLog.write("0.Load messages from file" + "\r\n");
        history.clear();
        try {
            String JSONData = Files.readAllLines(Paths.get("history.json")).toString();
            JsonReader forRead = Json.createReader(new StringReader(JSONData));
            JsonArray forArray = forRead.readArray();
            if (forArray.size() == 0) {
                System.out.println("Your history is empty");
                return;
            }
            JsonArray array = forArray.getJsonArray(0);
            forRead.close();
            for (int i = 0; i < array.size(); i++) {
                JsonObject tmpObject = array.getJsonObject(i);
                Date tmpTime = new Date(tmpObject.getJsonNumber("timestamp").longValue());
                Message tempMessage = new Message(tmpObject.getString("id"), tmpObject.getString("author"), tmpTime,
                        tmpObject.getString("message"));
                history.add(tempMessage);
            }

            System.out.println("Successfully done");
            writeLog.write("Successfully done" + "\r\n");
        } catch (NoSuchFileException e) {
            System.out.println("No such file " + e.getMessage());
            writeLog.write("No such file " + e.getMessage() + "\r\n");
        }
    }

    public void saveMessages(FileWriter writeLog) throws IOException {
        writeLog.write("1.Save messages in file" + "\r\n");
        if (!history.isEmpty()) {
            FileWriter out = new FileWriter("history.json");
            JsonWriter writeHistory = Json.createWriter(out);
            JsonArrayBuilder wrightArray = Json.createArrayBuilder();
            for (Message aHistory : history) {
                wrightArray.add(Json.createObjectBuilder().add("id", aHistory.getId())
                        .add("author", aHistory.getAuthor())
                        .add("timestamp", aHistory.getTimestamp().getTime())
                        .add("message", aHistory.getMessage()).build());
            }
            JsonArray arr = wrightArray.build();
            writeHistory.writeArray(arr);
            out.close();
            writeHistory.close();
            System.out.println("Successfully done");
            writeLog.write("Successfully done" + "\r\n");
        } else {
            System.out.println("Firstly download message history");
            writeLog.write("Firstly download message history" + "\r\n");
        }
    }

    public void addMessage(FileWriter writeLog) throws IOException {
        writeLog.write("2.Add a message" + "\r\n");
        Scanner sc = new Scanner(System.in);
        Date tmpDate = new Date();

        System.out.println("input your name");
        String name = sc.nextLine();
        System.out.println("input your message");
        String message = sc.nextLine();
        Message tempMessage = new Message("random-id-" + (new Random()).nextInt(), name, tmpDate, message);
        history.add(tempMessage);
        System.out.println("Successfully added");
        writeLog.write("Successfully added 1 message" + "\r\n");
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

    public void deleteMessage(FileWriter writeLog) throws IOException {
        writeLog.write("4.Delete message" + "\r\n");
        Scanner sc = new Scanner(System.in);
        String idNeed;
        boolean find = false;
        int count = 0;

        System.out.println("Input need id");
        idNeed = sc.next();
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).getId().equals(idNeed)) {
                history.remove(i);
                find = true;
                count++;
            }
        }
        if (find) {
            System.out.println("Successfully done");
        } else {
            System.out.println("there is no such message");
        }
        writeLog.write("Deleted " + count + " message(s)" + "\r\n");
    }

    public void searchByAuthor(FileWriter writeLog) throws IOException {
        writeLog.write("5.Search message by author" + "\r\n");
        Scanner sc = new Scanner(System.in);
        String author;
        boolean find = false;
        int count = 0;

        System.out.println("input author");
        author = sc.nextLine();
        for (Message i : history) {
            if (i.getAuthor().equals(author)) {
                System.out.println(i.toString());
                find = true;
                count++;
            }
        }
        if (find) {
            System.out.println("Successfully done");
        } else {
            System.out.println("No message from this author");
        }
        writeLog.write(count + " message(s) found" + "\r\n");
    }

    public void searchByWord(FileWriter writeLog) throws IOException {
        writeLog.write("6.Search message by word" + "\r\n");
        Scanner sc = new Scanner(System.in);
        String word;
        boolean find = false;
        int count = 0;

        System.out.println("input word");
        word = sc.next();
        for (Message it : history) {
            if (it.getMessage().contains(word)) {
                System.out.println(it.toString());
                find = true;
                count++;
            }
        }
        if (find) {
            System.out.println("Successfully done");
        } else {
            System.out.println("No message with this word");
        }
        writeLog.write(count + " message(s) found" + "\r\n");
    }

    public void searchByExpression(FileWriter writeLog) throws IOException {
        writeLog.write("7.Search message by regular expression" + "\r\n");
        Scanner sc = new Scanner(System.in);
        boolean find = false;
        String expression;
        int count = 0;

        System.out.println("input regular expression");
        expression = sc.nextLine();
        Pattern pat = Pattern.compile(expression);
        for (Message it : history) {
            Matcher matcher = pat.matcher(it.getMessage());
            if (matcher.find()) {
                find = true;
                count++;
                System.out.println(it.toString());
            }
        }
        if (!find) {
            System.out.println("There are no messages with this regular expression");
        }
        writeLog.write(count + " message(s) found" + "\r\n");
    }

    public void searchByTime(FileWriter writeLog) throws IOException {
        writeLog.write("8.Search message by time period" + "\r\n");
        Scanner sc = new Scanner(System.in);
        boolean find = false;
        System.out.println("input start time in format: MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat startTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date startDate;
        Date endDate;
        int count = 0;
        try {
            startDate = startTime.parse(sc.nextLine());
            System.out.println("input end time in format: MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat endTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            endDate = endTime.parse(sc.nextLine());
            for (Message it : history) {
                if (it.getTimestamp().after(startDate) && it.getTimestamp().before(endDate)) {
                    System.out.println(it.toString());
                    find = true;
                    count++;
                }
            }
            if (find) {
                System.out.println("Successfully done");
            } else {
                System.out.println("No message of this period");
            }
            writeLog.write(count + " message(s) found" + "\r\n");
        } catch (ParseException e) {
            System.out.println("Unparseable date " + e.getMessage());
            writeLog.write("Unparseable date " + e.getMessage() + "\r\n");
        }
    }
}
