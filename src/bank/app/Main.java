package bank.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        Connection dbConnection = DBConnection.getConnection();
        Bank fabulousBank = null;
        System.out.println("Welcome to Fabulous Bank!");
        int choice = 0;
        do {
            printMainMenuInstructions();
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        fabulousBank = new Bank(dbConnection);
                        boolean successfulLogin = fabulousBank.login();
                        if(successfulLogin){
                            getBankMenu(dbConnection, fabulousBank);
                        }
                    break;
                    case 2:
                        fabulousBank = new Bank(dbConnection);
                        fabulousBank.registerUser();
                        getBankMenu(dbConnection, fabulousBank);
                    break;
                    case 3:
                        System.out.println("Goodbye!");
                    break;
                    default:
                        System.out.println("Input not valid (1-3)");
                    break;
                }
            }
            catch (InputMismatchException e){
                System.out.println("Input not valid (1-3)");
            }
        } while (choice != 3);
    }

    private static void getBankMenu(Connection connection, Bank fabulousBank){
        int choice = 0;
        do {
            printBankInstructions();
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("Your balance is " + fabulousBank.getBalance() + " EUR");
                        break;
                    case 2:
                        creditMenu(fabulousBank);
                        break;
                    case 3:
                        debitMenu(fabulousBank);
                        break;
                    case 4:
                        fabulousBank = null;
                        break;
                    case 5:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Input not valid (1-5)");
                        break;
                }
            }
            catch (InputMismatchException e){
                System.out.println("Input not valid (1-5)");
            }
        } while (choice != 4 && choice != 5);
    }
    public static void printMainMenuInstructions(){
        System.out.println("Do you want to login or register a new user ?");
        System.out.println("\t 1 - login");
        System.out.println("\t 2 - register a new user");
        System.out.println("\t 3 - quit application");
    }

    public static void creditMenu(Bank fabulousBank){
        System.out.println("Enter amount to credit your account: ");
        try{
            double creditAmount = scanner.nextDouble();
            fabulousBank.creditUser(creditAmount);
            System.out.println("Your balance is " + fabulousBank.getBalance() + " EUR");
        }
        catch (Exception e){
            System.out.println("Input not valid");
        }
    }

    public static void debitMenu(Bank fabulousBank){
        System.out.println("Enter amount to debit from your account: ");
        try{
            double creditAmount = scanner.nextDouble();
            fabulousBank.debitUser(creditAmount);
            System.out.println("Your balance is " + fabulousBank.getBalance() + " EUR");
        }
        catch (Exception e){
            System.out.println("Input not valid");
        }
    }

    public static void printBankInstructions(){
        System.out.println("Press");
        System.out.println("\t 1 - To show user balance");
        System.out.println("\t 2 - Add funds to the account (credit account)");
        System.out.println("\t 3 - Retrieve funds from the account (debit account)");
        System.out.println("\t 4 - To log out");
        System.out.println("\t 5 - To quit application");
    }
}
