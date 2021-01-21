package banking;

public enum State {
    MENU_START("1. Create an account\n" +
            "2. Log into account\n" +
            "0. Exit"),
    MENU_ACCOUNT("1. Balance\n" +
            "2. Add income\n" +
            "3. Do transfer\n" +
            "4. Close account\n" +
            "5. Log out\n" +
            "0. Exit"),
    ASK_CARD_NUMBER("Enter your card number:"),
    ASK_PIN("Enter your PIN:"),
    MESSAGE_CARD_CREATED("Your card has been created\n" +
            "Your card number:\n" +
            "%s\n" +
            "Your card PIN:\n" +
            "%s"),
    MESSAGE_WRONG("Wrong card number or PIN!"),
    MESSAGE_SUCCESSFUL("You have successfully logged in!"),
    MESSAGE_BALANCE("Balance: %d"),
    MESSAGE_LOGOUT("You have successfully logged out!"),
    MESSAGE_EXIT("Bye!"),
    ASK_DEPOSIT("Enter income:"),
    ASK_TRANSFER_CARD_NUMBER("Transfer\n" +
            "Enter card number:"),
    ASK_TRANSFER_AMOUNT("How much money would you like to transfer?"),
    MESSAGE_ACCOUNT_CLOSED("The account has been closed!"),
    MESSAGE_DEPOSIT_SUCCESS("Income was added!"),
    MESSAGE_TRANSFER_SUCCESS("Success!"),
    MESSAGE_TRANSFER_NOT_ENOUGH("Not enough money!"),
    MESSAGE_TRANSFER_SAME_ACCOUNT("You can't transfer money to the same account!"),
    MESSAGE_TRANSFER_WRONG_CARD("Probably you made mistake in the card number. Please try again!"),
    MESSAGE_TRANSFER_NOT_EXIST("Such a card does not exist.");

    private String message;

    State(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
