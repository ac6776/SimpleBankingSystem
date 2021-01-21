package banking;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String fileName = args.length == 2 ? args[1] : "db.s3db";

        CardRepo repo = new CardRepo(fileName);
        MessageService messageService = MessageService.getInstance();

        Scanner scanner = new Scanner(System.in);
        State state = State.MENU_START;
        String card = "";
        Card currentAccount = null;
        Card cardTo = null;

        messageService.add(state.getMessage());
        while (true) {
            messageService.print();
            if (state == State.MESSAGE_EXIT) {
                return;
            }

            String input = scanner.nextLine();

            switch (state) {
                case MENU_START:
                    switch (input) {
                        case "1":
                            String generateCardNumber = generateCardNumber();
                            String generatedPin = generatePin();
                            repo.save(generateCardNumber, generatedPin);
                            messageService.add(String.format(State.MESSAGE_CARD_CREATED.getMessage(), generateCardNumber, generatedPin));
                            break;
                        case "2":
                            state = State.ASK_CARD_NUMBER;
                            break;
                        case "0":
                            state = State.MESSAGE_EXIT;
                            break;
                        default:
                            messageService.add("Wrong input");
                            break;
                    }
                    messageService.add(state.getMessage());
                    break;
                case ASK_CARD_NUMBER:
                    card = input;
                    state = State.ASK_PIN;
                    messageService.add(state.getMessage());
                    break;
                case ASK_PIN:
                    String pin = input;
                    Card cardFromDB = repo.getCardByNumber(card);
                    if (cardFromDB != null) {
                        if (pin.equals(cardFromDB.getPin())) {
                            currentAccount = cardFromDB;
                        } 
                    }
                    if (currentAccount != null) {
                        state = State.MENU_ACCOUNT;
                        messageService.add(State.MESSAGE_SUCCESSFUL.getMessage());
                    } else {
                        state = State.MENU_START;
                        messageService.add(State.MESSAGE_WRONG.getMessage());
                    }
                    messageService.add(state.getMessage());
                    break;
                case ASK_DEPOSIT:
                    int amount = Integer.parseInt(input);
                    repo.deposit(currentAccount, amount);
                    currentAccount.setBalance(currentAccount.getBalance() + amount);
                    state = State.MENU_ACCOUNT;
                    messageService.add(State.MESSAGE_DEPOSIT_SUCCESS.getMessage());
                    messageService.add(state.getMessage());
                    break;
                case ASK_TRANSFER_CARD_NUMBER:
                    if (!isValid(input)) {
                        messageService.add(State.MESSAGE_TRANSFER_WRONG_CARD.getMessage());
                        state = State.MENU_ACCOUNT;
                        break;
                    }
                    cardTo = repo.getCardByNumber(input);
                    if (cardTo == null) {
                        messageService.add(State.MESSAGE_TRANSFER_NOT_EXIST.getMessage());
                        state = State.MENU_ACCOUNT;
                        break;
                    }
                    state = State.ASK_TRANSFER_AMOUNT;
                    messageService.add(state.getMessage());
                    break;
                case ASK_TRANSFER_AMOUNT:
                    amount = Integer.parseInt(input);
                    if (currentAccount.getId() == cardTo.getId()) {
                        messageService.add(State.MESSAGE_TRANSFER_SAME_ACCOUNT.getMessage());
                        state = State.MENU_ACCOUNT;
                        break;
                    }
                    if (!repo.transfer(currentAccount, cardTo, amount)) {
                        messageService.add(State.MESSAGE_TRANSFER_NOT_ENOUGH.getMessage());
                        state = State.MENU_ACCOUNT;
                        break;
                    }
                    messageService.add(State.MESSAGE_TRANSFER_SUCCESS.getMessage());
                    state = State.MENU_ACCOUNT;
                    break;
                case MENU_ACCOUNT:
                    switch (input) {
                        case "1":
                            currentAccount.setBalance(repo.getBalance(currentAccount.getNumber()));
                            messageService.add(String.format(State.MESSAGE_BALANCE.getMessage(), currentAccount.getBalance()));
                            break;
                        case "2":
                            state = State.ASK_DEPOSIT;
                            break;
                        case "3":
                            state = State.ASK_TRANSFER_CARD_NUMBER;
                            break;
                        case "4":
                            repo.delete(currentAccount);
                            state = State.MENU_START;
                            messageService.add(State.MESSAGE_ACCOUNT_CLOSED.getMessage());
                            break;
                        case "5":
                            messageService.add(State.MESSAGE_LOGOUT.getMessage());
                            currentAccount = null;
                            state = State.MENU_START;
                            break;
                        case "0":
                            state = State.MESSAGE_EXIT;
                            break;
                        default:
                            messageService.add("Wrong input");
                            break;
                    }
                    messageService.add(state.getMessage());
                    break;

            }
        }
    }

    public static String generateCardNumber() {
        StringBuilder sb = new StringBuilder("400000");
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(9));
        }
        String generated = sb.toString();
        sb.setLength(0);
        for (int i = 0; i < 15; i++) {
            int num = Integer.parseInt(String.valueOf(generated.charAt(i)));
            if (i % 2 == 0) {
                num *= 2;
            }
            if (num > 9) {
                num -= 9;
            }
            sb.append(num);
        }
        int res = 0;
        for (int i = 0; i < 15; i++) {
            int num = Integer.parseInt(String.valueOf(sb.charAt(i)));
            res += num;
        }
        sb.setLength(0);
        sb.append(generated);
        int remainder = res % 10;
        if (remainder == 0) {
            sb.append("0");
        } else {
            sb.append(10 - remainder);
        }
        return sb.toString();
    }

    public static String generatePin() {
        StringBuilder sb = new StringBuilder(4);
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }

    public static boolean isValid(String number) {
        if (number.length() != 16) {
            return false;
        }
        int last = Character.digit(number.charAt(15), 10);
        int res = 0;
        for (int i = 0; i < 15; i++) {
            int num = Integer.parseInt(String.valueOf(number.charAt(i)));
            if (i % 2 == 0) {
                num *= 2;
            }
            if (num > 9) {
                num -= 9;
            }
            res += num;
        }
        int remainder = res % 10;
        int lastAssert = remainder == 0 ? 0 : (10 - remainder);
        return last == lastAssert;
    }
}