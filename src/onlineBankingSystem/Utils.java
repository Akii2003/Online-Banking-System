package onlineBankingSystem;

import java.util.regex.Pattern;

public class Utils {

    // Method to validate email
    public static boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    // Method to validate password
    public static boolean validatePassword(String password) {
        return password.length() >= 8; // You can add more rules if needed
    }

    // Method to generate random account number
    public static long generateAccountNumber() {
        return (long) (Math.random() * 10000000000L);
    }
}
