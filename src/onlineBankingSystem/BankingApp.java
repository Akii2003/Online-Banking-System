package onlineBankingSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {

    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "@Shubhamsql1";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Scanner scanner = new Scanner(System.in);
            UserDAO userDAO = new UserDAO(connection);
            AccountsDAO accountsDAO = new AccountsDAO(connection);
            AccountManager accountManager = new AccountManager(connection, scanner);

            while (true) {
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");

                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        registerUser(userDAO, scanner);
                        break;
                    case 2:
                        loginUser(userDAO, accountsDAO, accountManager, scanner);
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registerUser(UserDAO userDAO, Scanner scanner) throws SQLException {
        System.out.println("Enter Name: ");
        String name = scanner.next();
        System.out.println("Enter Email: ");
        String email = scanner.next();
        System.out.println("Enter Password: ");
        String password = scanner.next();

        if (!Utils.validateEmail(email)) {
            System.out.println("Invalid Email Format!");
            return;
        }

        if (!Utils.validatePassword(password)) {
            System.out.println("Password must be at least 8 characters!");
            return;
        }

        if (!userDAO.isUserExist(email)) {
            userDAO.registerUser(new User(name, email, password));
            System.out.println("User Registered Successfully!");
        } else {
            System.out.println("User Already Exists!");
        }
    }

    private static void loginUser(UserDAO userDAO, AccountsDAO accountsDAO, AccountManager accountManager, Scanner scanner) throws SQLException {
        System.out.println("Enter Email: ");
        String email = scanner.next();
        System.out.println("Enter Password: ");
        String password = scanner.next();

        User user = userDAO.loginUser(email, password);

        if (user != null) {
            System.out.println();
            System.out.println("User Logged In!");

            // Check if the user has an account
            if (!accountsDAO.accountExists(email)) {
            	  System.out.println();
                  System.out.println("1. Open a new Bank Account");
                  System.out.println("2. Exit");
            
            
            if(scanner.nextInt() == 1) {
                System.out.println("Enter the initial balance for your account: ");
                double initialBalance = scanner.nextDouble();  // Get the initial balance from user input
                
                System.out.println("Enter the pin for your account: ");
               int pin = scanner.nextInt();

                // Call the openAccount method with the email and initialBalance
                long accountNumber = accountsDAO.openAccount(email, initialBalance,pin);

                System.out.println("Account Created Successfully");
                System.out.println("Your Account Number is: " + accountNumber);
            } 
            }
            else if(accountsDAO.accountExists(email)){
                System.out.println("Account already exists for this user.");
                
                int choice;
                long accountNumber = accountsDAO.getAccountNumber(email); // Get the account number after successful login

                do {
                    System.out.println();
                    System.out.println("1. Debit Money");
                    System.out.println("2. Credit Money");
//                    System.out.println("3. Transfer Money");
                    System.out.println("3. Check Balance");
                    System.out.println("4. Log Out");
                    System.out.print("Enter your choice: ");
                    choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            accountManager.debit_money(accountNumber);
                            break;
                        case 2:
                            accountManager.credit_money(accountNumber);
                            break;
//                        case 3:
//                            accountManager.transfer_money(accountNumber);
//                            break;
                        case 3:
                            accountManager.getBalance(accountNumber);
                            break;
                        case 4:
                            System.out.println("Logging out...");
                            break;
                        default:
                            System.out.println("Invalid choice, please try again.");
                            break;
                    }
                } while (choice != 4); // Continue until the user chooses to log out

            }
            
            }
        else {
            System.out.println("Incorrect Email or Password!");
        }

            // Further actions after the user is logged in and account handling
           
    }

}
