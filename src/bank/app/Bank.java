package bank.app;

import java.sql.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bank.app.Main.scanner;

public class Bank {
    private Connection dbConnection;
    private User user;

    public Bank(Connection dbConnection){
        this.dbConnection = dbConnection;
    }

    public boolean login() {
        scanner.nextLine();
        System.out.println("Enter account number");
        String existingUserAccountNumber = scanner.nextLine();

        System.out.println("Enter password");
        String existingUserPassword = scanner.nextLine();

        String passwordHash = Sha256.generateSHA256(existingUserPassword);

        User loggedInUser = getUser(existingUserAccountNumber,passwordHash);

        if (loggedInUser != null) {
            System.out.println("You have logged in successfully!");
            this.user = loggedInUser;
            return true;
        }
        System.out.println("Incorrect account number or password!");
        return false;
    }

    public void registerUser() {
        this.user = new User();
        this.user.setAccountNumber(generateAccountNumber());
        scanner.nextLine();
        System.out.println("Please, enter first name and last name");
        this.user.setUserFullName(Main.scanner.nextLine());

        Pattern fullNamePattern = Pattern.compile("^([A-Za-z]*((\\s)))+[A-Za-z]*$");
        Matcher fullNameMatcher = fullNamePattern.matcher(this.user.getUserFullName());
        while (fullNameMatcher.matches()== false) {
            System.out.println("Please, enter valid first name and last name!");
            this.user.setUserFullName(Main.scanner.nextLine());
            fullNameMatcher = fullNamePattern.matcher(this.user.getUserFullName());
        }

        System.out.println("Please, enter password. It should be at least 6 characters");
        String password = Main.scanner.nextLine();

        Pattern passwordPattern = Pattern.compile("[a-zA-Z0-9+_.!-]{6,20}");
        Matcher passwordMatcher = passwordPattern.matcher(password);
        while (passwordMatcher.matches()== false) {
            System.out.println("Please enter a valid password! It should be at least 6 characters!");
            password = Main.scanner.nextLine();
            passwordMatcher = passwordPattern.matcher(password);
        }
        this.user.setPasswordHash(Sha256.generateSHA256(password));

        System.out.println("Choose gender");
        printGenderMenu();
        String gender = "";
        int genderChoice = Main.scanner.nextInt();
        boolean isValidGenderChoice = false;
        Main.scanner.nextLine();

        while (!isValidGenderChoice)
        switch (genderChoice) {
            case 1:
                gender = "Male";
                isValidGenderChoice = true;
            break;
            case 2:
                gender = "Female";
                isValidGenderChoice = true;
            break;
            case 3:
                gender = "Non-binary";
                isValidGenderChoice = true;
            break;
            default:
                System.out.println("Input not valid (1-3)");
                isValidGenderChoice = false;
            break;
        }
        this.user.setGender(gender);
        this.user.setBalance(0);
        createUser(this.user);
        System.out.println("Your Account number is " + this.user.getAccountNumber());
    }

    public void creditUser(double creditAmount) {
        double currentBalance = this.user.getBalance();
        this.user.setBalance(currentBalance + creditAmount);
        updateBalance(this.user.getBalance());
    }

    public void debitUser(double debitAmount) {
        double currentBalance = this.user.getBalance();
        double debitBalance = currentBalance - debitAmount;
        if (debitBalance < 0) {
            System.out.println("Insufficient funds");
        }
        else {
            this.user.setBalance(debitBalance);
            updateBalance(this.user.getBalance());
        }

    }

    public double getBalance() {
        return this.user.getBalance();
    }

    private void printGenderMenu(){
        System.out.println("\nPress");
        System.out.println("\t 1 - Male");
        System.out.println("\t 2 - Female");
        System.out.println("\t 3 - Non-binary");
    }

    private String generateAccountNumber() { // LV + 8 CAPS Letters or numbers
        int length = 8;
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char c;
            // Generate a random number between 0 and 35
            int randomNumber = random.nextInt(36);
            if (randomNumber < 10) {
                c = (char) ('0' + randomNumber);
            } else {
                c = (char) ('A' + randomNumber - 10);
            }
            sb.append(c);
        }
        String accountNumber = "LV" + sb.toString();
        return accountNumber;
    }

    private void createUser(User newUser) {
        try {
            String sql = "INSERT INTO Users (accountNumber, passwordHash, fullName, balance, gender) VALUES (?,?,?,?,?)";

            PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
            preparedStatement.setString(1, newUser.getAccountNumber());
            preparedStatement.setString(2, newUser.getPasswordHash());
            preparedStatement.setString(3, newUser.getUserFullName());
            preparedStatement.setDouble(4, newUser.getBalance());
            preparedStatement.setString(5, newUser.getGender());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User getUser(String accountNumber, String passwordHash) {
        try {
            String sql = "SELECT * FROM Users WHERE accountNumber ='" + accountNumber + "' and passwordHash ='" + passwordHash + "'";

            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                User user = new User();
                user.setUserFullName(resultSet.getString("fullName"));
                user.setBalance(resultSet.getDouble("balance"));
                user.setAccountNumber(resultSet.getString("accountNumber"));
                user.setGender(resultSet.getString("gender"));
                return user;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateBalance(double newBalance){
        try {
            String sql = "UPDATE Users SET balance = ? WHERE accountNumber = ?";

            PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, this.user.getAccountNumber());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

