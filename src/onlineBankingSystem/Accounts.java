package onlineBankingSystem;





public class Accounts {
    private long accountNumber;
    private String email;
    private double balance;
    private int pin;
    

    public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public Accounts(long accountNumber, String email, double balance) {
        this.accountNumber = accountNumber;
        this.email = email;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

	
}
