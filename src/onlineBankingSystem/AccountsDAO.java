package onlineBankingSystem;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountsDAO {
    
    private final Connection connection;

    public AccountsDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to check if the account exists for a user based on email
    public boolean accountExists(String email) throws SQLException {
        String query = "SELECT * FROM accounts WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    // Method to open a new account for the user
    public long openAccount(String email, double initialBalance,int pin) throws SQLException {
        long accountNumber = Utils.generateAccountNumber();
        String query = "INSERT INTO accounts (account_number, email, balance,pin) VALUES (?, ?, ?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setLong(1, accountNumber);
        ps.setString(2, email);
        ps.setDouble(3, initialBalance);
        ps.setInt(4,pin);
        ps.executeUpdate();
        return accountNumber;
    }

    // Method to get the account number based on the email
    public long getAccountNumber(String email) throws SQLException {
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getLong("account_number");
        }
        throw new SQLException("Account not found for the given email!");
    }

    // Method to get the account balance based on the account number
    public double getBalance(long accountNumber) throws SQLException {
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setLong(1, accountNumber);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getDouble("balance");
        }
        throw new SQLException("Account not found for the given account number!");
    }

    // Method to update the balance (for debit/credit operations)
    public void updateBalance(long accountNumber, double newBalance) throws SQLException {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setDouble(1, newBalance);
        ps.setLong(2, accountNumber);
        ps.executeUpdate();
    }

    // Method to transfer money from one account to another
    public void transferMoney(long fromAccountNumber, long toAccountNumber, double amount) throws SQLException {
        // Start a transaction
        connection.setAutoCommit(false);
        
        try {
            // Debit the amount from the sender's account
            double fromBalance = getBalance(fromAccountNumber);
            if (fromBalance < amount) {
                throw new SQLException("Insufficient balance for transfer!");
            }
            updateBalance(fromAccountNumber, fromBalance - amount);

            // Credit the amount to the receiver's account
            double toBalance = getBalance(toAccountNumber);
            updateBalance(toAccountNumber, toBalance + amount);

            // Commit the transaction
            connection.commit();
        } catch (SQLException e) {
            // If any error occurs, rollback the transaction
            connection.rollback();
            throw e;
        } finally {
            // Reset auto-commit mode
            connection.setAutoCommit(true);
        }
    }
}

