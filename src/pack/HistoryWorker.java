package pack;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HistoryWorker {
    final static String LOG_FILE_NAME = "logfile.txt";
    private FileWriter log;
    private BufferedReader read;
    private History history;

    public HistoryWorker() {
        history = new History();
        try {
            log = new FileWriter(LOG_FILE_NAME, true);
            read = new BufferedReader(new InputStreamReader(System.in));
            String choice = "";
            showVariants();
            while (!choice.equals("9")) {
                choice = read.readLine();
                switch (choice) {
                    case "0": {
                        loadMessages();
                        break;
                    }
                    case "1": {
                        saveMessages();
                        break;
                    }
                    case "2": {
                        addMessage();
                        break;
                    }
                    case "3": {
                        history.showHistory();
                        break;
                    }
                    case "4": {
                        deleteMessage();
                        break;
                    }
                    case "5": {
                        searchByAuthor();
                        break;
                    }
                    case "6": {
                        searchByWord();
                        break;
                    }
                    case "7": {
                        searchByExpression();
                        break;
                    }
                    case "8": {
                        searchByTime();
                        break;
                    }
                    case "9": {
                        log.write("End of program" + "\r\n");
                        System.out.println("End of program");
                        break;
                    }
                    default: {
                        System.out.println("You need number from 0 to 9");
                        break;
                    }
                }
            }
            read.close();
            log.close();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        }
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

    public void loadMessages() throws IOException {
        log.write("0.Load messages from file" + "\r\n");
        try {
            JsonArray forArray = getJson();
            if (forArray.size() == 0) {
                System.out.println("Your history is empty");
                return;
            }
            JsonArray array = forArray.getJsonArray(0);
            history.loadMessages(array);
            System.out.println("Successfully done");
            log.write("Successfully done" + "\r\n");
        } catch (NoSuchFileException e) {
            System.out.println("No such file " + e.getMessage());
            log.write("No such file " + e.getMessage() + "\r\n");
        }
    }

    public JsonArray getJson() throws IOException {
        String JSONData = Files.readAllLines(Paths.get("history.json")).toString();
        JsonReader forRead = Json.createReader(new StringReader(JSONData));
        JsonArray forArray = forRead.readArray();
        forRead.close();
        return forArray;
    }

    public void saveMessages() throws IOException {
        log.write("1.Save messages in file" + "\r\n");
        if (!history.isEmpty()) {
            JsonArray arr = history.createArray();
            history.saveMessages(arr);
            System.out.println("Successfully done");
            log.write("Successfully done" + "\r\n");
        } else {
            System.out.println("Firstly download message history");
            log.write("Firstly download message history" + "\r\n");
        }
    }

    public void addMessage() throws IOException {
        log.write("2.Add a message" + "\r\n");
        Date tmpDate = new Date();

        System.out.println("input your name");
        String name = read.readLine();
        System.out.println("input your message");
        String message = read.readLine();
        history.addMessage(new Message("random-id-" + (new Random()).nextInt(), name, tmpDate, message));
        System.out.println("Successfully added");
        log.write("Successfully added 1 message" + "\r\n");
    }

    public void deleteMessage() throws IOException {
        log.write("4.Delete message" + "\r\n");
        int count;

        System.out.println("Input need id");
        count = history.deleteMessage(read.readLine());
        if (count != 0) {
            System.out.println("Successfully done");
        } else {
            System.out.println("there is no such message");
        }
        log.write("Deleted " + count + " message(s)" + "\r\n");
    }

    public void searchByAuthor() throws IOException {
        log.write("5.Search message by author" + "\r\n");
        int count;
        System.out.println("input author");
        count = history.searchByAuthor(read.readLine());

        if (count != 0) {
            System.out.println("Successfully done");
        } else {
            System.out.println("No message from this author");
        }
        log.write(count + " message(s) found" + "\r\n");
    }

    public void searchByWord() throws IOException {
        log.write("6.Search message by word" + "\r\n");
        int count;

        System.out.println("input word");
        count = history.searchByWord(read.readLine());
        if (count != 0) {
            System.out.println("Successfully done");
        } else {
            System.out.println("No message with this word");
        }
        log.write(count + " message(s) found" + "\r\n");
    }

    public void searchByExpression() throws IOException {
        log.write("7.Search message by regular expression" + "\r\n");
        int count;

        System.out.println("input regular expression");
        count = history.searchByExpression(read.readLine());
        if (count == 0) {
            System.out.println("There are no messages with this regular expression");
        }
        log.write(count + " message(s) found" + "\r\n");
    }

    public void searchByTime() throws IOException {
        log.write("8.Search message by time period" + "\r\n");
        SimpleDateFormat startTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat endTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date startDate;
        Date endDate;
        int count;
        System.out.println("input start time in format: MM/dd/yyyy HH:mm:ss");
        try {
            startDate = startTime.parse(read.readLine());
            System.out.println("input end time in format: MM/dd/yyyy HH:mm:ss");
            endDate = endTime.parse(read.readLine());
            count = history.searchByTime(startDate, endDate);
            if (count != 0) {
                System.out.println("Successfully done");
            } else {
                System.out.println("No message of this period");
            }
            log.write(count + " message(s) found" + "\r\n");
        } catch (ParseException e) {
            System.out.println("Unparseable date " + e.getMessage());
            log.write("Unparseable date " + e.getMessage() + "\r\n");
        }
    }
}
