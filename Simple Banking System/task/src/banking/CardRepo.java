package banking;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CardRepo {
    private DBService service;

    public CardRepo(String dbName) {
        service = DBService.getInstance(dbName);
        service.modifyData("CREATE TABLE IF NOT EXISTS card(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "number VARCHAR(16) NOT NULL," +
                "pin VARCHAR(4) NOT NULL," +
                "balance INTEGER DEFAULT 0)");
    }

    public void save(String number, String pin) {
        service.modifyData(String.format("INSERT INTO card (number, pin) VALUES ('%s', '%s')", number, pin));
    }

    public Card getCardByNumber(String number) {
        Card card = null;
        try (ResultSet rs = service.extractData(String.format("SELECT * FROM card WHERE number = %s", number))) {
            int id = rs.getInt("id");
            String num = rs.getString("number");
            String pin = rs.getString("pin");
            int balance = rs.getInt("balance");
            card = new Card(id, num, pin, balance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    public Integer getBalance(String number) {
        try (ResultSet rs = service.extractData(String.format("SELECT * FROM card WHERE number = %s", number))) {
            return rs.getInt("balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deposit(Card card, int amount) {
        service.modifyData(String.format("UPDATE card SET balance = balance + %d WHERE id = %d", amount, card.getId()));
    }

    public void delete(Card card) {
        service.modifyData(String.format("DELETE FROM card WHERE id = %d", card.getId()));
    }

    public boolean transfer(Card cardFrom, Card cardTo, int amount) {
        try(Connection connection = service.getConnection();
            Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            int res = statement.executeUpdate(String.format("UPDATE card SET balance = balance - %d WHERE id = %d AND balance - %d >= 0", amount, cardFrom.getId(), amount));
            statement.executeUpdate(String.format("UPDATE card SET balance = balance + %d WHERE id = %d", amount, cardTo.getId()));
            if (res == 0) {
                connection.rollback();
                return false;
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
