package bank.app; //TODO
        //Users can register,
        //bank.app.User class should have basic information like account number, balance, name, gender etc.
        //bank.app.Bank class should be able to accept a single user at a time, and have methods to credit and debit the user as well as show the user balance.
        //Should be a method to replace existing users with new one in the bank.
        //All classes should be used from main class

public class User {

    private String accountNumber;
    private String passwordHash;
    private String userFullName;
    private double balance;
    private String gender;



    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
