package banking;

import java.util.LinkedList;
import java.util.List;

public class MessageService {
    private static MessageService service;
    private List<String> messages;

    private MessageService() {
        messages = new LinkedList<>();
    }

    public static MessageService getInstance() {
        if (service == null) {
            return new MessageService();
        }
        return service;
    }

    public void add(String message) {
        messages.add(message);
    }

    public void print() {
        for (String message : messages) {
            System.out.println("\n" + message);
        }
        messages.clear();
    }
}
