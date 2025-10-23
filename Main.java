import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    static final String FILE_NAME = "User_Data.txt";
    static final String TRANSACTION_FILE = "Transaction_History.txt";
    static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("WELCOME TO Z-U-A Bank");

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }

        while (true) {
            Main_Menu();
            int choice = Input_int("Enter your choice: ");

            switch (choice) {
                case 1 -> Create_Account();
                case 2 -> Deposit();
                case 3 -> Withdraw();
                case 4 -> Check_Transaction();
                case 5 -> Get_Account_Details();
                case 6 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }

    // ============================= INPUT METHODS =============================

    static String Input_String(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    static int Input_int(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    static double Input_double(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    // ============================= MENU =============================

    static void Main_Menu() {
        System.out.println("\n==============================");
        System.out.println("          MAIN MENU");
        System.out.println("==============================");
        System.out.println("1: Create Account");
        System.out.println("2: Deposit");
        System.out.println("3: Withdraw");
        System.out.println("4: Check Transaction");
        System.out.println("5: Get Account Details");
        System.out.println("6: Exit");
    }

    // ============================= ACCOUNT CREATION =============================

    static void Create_Account() {
        System.out.println("\n--- Creating Account ---");

        String name = Input_String("Enter Your Name: ");
        String cnic = Input_String("Enter Your CNIC: ");
        String contact = Input_String("Enter Your Contact Number: ");
        String email = Input_String("Enter Your Email: ");
        String address = Input_String("Enter Your Address: ");
        int pin = Input_int("Set Your 4-digit PIN: ");

        String accountNumber = Generate_Account_Number();
        double balance = 0.0;

        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write("Account Holder Name: " + name + "\n");
            fw.write("Account Holder CNIC: " + cnic + "\n");
            fw.write("Account Holder Contact: " + contact + "\n");
            fw.write("Account Holder Email: " + email + "\n");
            fw.write("Account Holder Address: " + address + "\n");
            fw.write("Account Holder PIN: " + pin + "\n");
            fw.write("Account Number: " + accountNumber + "\n");
            fw.write("Balance: " + balance + "\n");
            fw.write("============================\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        System.out.println("\nAccount Created Successfully!");
        System.out.println("Your Account Number is: " + accountNumber);
        System.out.println("Account data saved to file successfully.");
    }

    static String Generate_Account_Number() {
        Random random = new Random();
        String accNum;
        do {
            accNum = "PK-" + (100000 + random.nextInt(900000));
        } while (Account_Exists(accNum));
        return accNum;
    }

    static boolean Account_Exists(String accNum) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Account Number: ") && line.contains(accNum)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    // ============================= DEPOSIT / WITHDRAW =============================

    static void Deposit() {
        System.out.println("\n--- Deposit ---");
        String accNum = Input_String("Enter your Account Number: ");
        double amount = Input_double("Enter amount to deposit: ");
        if (amount <= 0) {
            System.out.println("Invalid amount. Must be greater than 0.");
            return;
        }

        if (Update_Balance(accNum, amount, true)) {
            Log_Transaction(accNum, "Deposit", amount);
            System.out.println("Deposited " + amount + " to Account " + accNum);
        } else {
            System.out.println("Account not found.");
        }
    }

    static void Withdraw() {
        System.out.println("\n--- Withdraw Funds ---");
        String accNum = Input_String("Enter your Account Number: ");
        double amount = Input_double("Enter amount to withdraw: ");
        if (amount <= 0) {
            System.out.println("Invalid amount. Must be greater than 0.");
            return;
        }

        if (Update_Balance(accNum, amount, false)) {
            Log_Transaction(accNum, "Withdraw", amount);
            System.out.println("Withdrawn " + amount + " from Account " + accNum);
        } else {
            System.out.println("Account not found or insufficient balance.");
        }
    }

    static boolean Update_Balance(String accNum, double amount, boolean isDeposit) {
        try {
            List<String> lines = new ArrayList<>();
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Account Number: ") && line.contains(accNum)) {
                        found = true;
                        lines.add(line);
                        String balanceLine = reader.readLine();
                        if (balanceLine != null && balanceLine.startsWith("Balance: ")) {
                            double currentBalance = Double.parseDouble(balanceLine.split(": ")[1]);
                            double newBalance = isDeposit ? currentBalance + amount : currentBalance - amount;

                            if (!isDeposit && newBalance < 0) {
                                System.out.println("Insufficient balance. Transaction cancelled.");
                                return false;
                            }
                            lines.add("Balance: " + newBalance);
                        }
                    } else {
                        lines.add(line);
                    }
                }
            }

            if (!found) return false;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (String l : lines) {
                    writer.write(l + "\n");
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }

    // ============================= TRANSACTION LOGGING =============================

    static void Log_Transaction(String accNum, String type, double amount) {
        try (FileWriter fw = new FileWriter(TRANSACTION_FILE, true)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            fw.write("Account Number: " + accNum + "\n");
            fw.write("Type: " + type + "\n");
            fw.write("Amount: " + amount + "\n");
            fw.write("Date: " + timestamp + "\n");
            fw.write("========================================\n");
        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }

    // ============================= CHECK TRANSACTIONS =============================

    static void Check_Transaction() {
        System.out.println("\n--- Check Transaction History ---");
        String accNum = Input_String("Enter your Account Number: ");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            System.out.println("\nTransaction History for Account: " + accNum);
            System.out.println("----------------------------------------");
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Account Number: ") && line.contains(accNum)) {
                    found = true;
                    System.out.println(line);
                    for (int i = 0; i < 3; i++) {
                        line = reader.readLine();
                        if (line == null || line.startsWith("========================================")) break;
                        System.out.println(line);
                    }
                    System.out.println("----------------------------------------");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transaction file: " + e.getMessage());
        }

        if (!found) {
            System.out.println("No transaction history found for this account.");
        }
    }

    // ============================= ACCOUNT DETAILS =============================

    static void Get_Account_Details() {
        System.out.println("\n--- Account Details ---");
        String accNum = Input_String("Enter your Account Number: ");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Account Number: ") && line.contains(accNum)) {
                    found = true;
                    System.out.println("\n--- Account Details ---");
                    for (int i = 0; i < 7; i++) {
                        System.out.println(line);
                        line = reader.readLine();
                        if (line == null || line.startsWith("============================")) break;
                    }
                    break;
                }
            }

            if (!found) {
                System.out.println("Account not found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading account details: " + e.getMessage());
        }
    }
}
