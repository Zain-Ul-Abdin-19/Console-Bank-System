import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.CardLayout;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main extends JFrame {

    // filenames (same as your CLI program)
    static final String FILE_NAME = "User_Data.txt";
    static final String TRANSACTION_FILE = "Transaction_History.txt";
    static final String LOGIN_FILE = "Owner_login.txt";

    // main UI components
    private CardLayout cards = new CardLayout();
    private JPanel cardPanel = new JPanel(cards);

    private JPanel loginPanel;
    private JPanel mainPanel;

    private JTextArea outputArea;

    public Main() {
        super("Z-U-A Bank");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Initialize_Login_File();
        ensureDataFilesExist();

        buildLoginPanel();
        buildMainPanel();

        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(mainPanel, "MAIN");

        add(cardPanel, BorderLayout.CENTER);

        cards.show(cardPanel, "LOGIN");
    }

    // ---------------- UI BUILDERS ----------------
    private void buildLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("OWNER LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(title, gbc);

        JLabel userLabel = new JLabel("Username:");
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridx = 0;
        loginPanel.add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        gbc.gridx = 1;
        loginPanel.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridy = 2; gbc.gridx = 0;
        loginPanel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(20);
        gbc.gridx = 1;
        loginPanel.add(passField, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        loginPanel.add(loginBtn, gbc);

        JLabel info = new JLabel("Default credentials: admin / admin123 (created automatically)", SwingConstants.CENTER);
        info.setFont(new Font("SansSerif", Font.PLAIN, 10));
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        loginPanel.add(info, gbc);

        loginBtn.addActionListener(_ -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (Validate_Credentials(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + username + "!", "OK", JOptionPane.INFORMATION_MESSAGE);
                cards.show(cardPanel, "MAIN");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void buildMainPanel() {
        mainPanel = new JPanel(new BorderLayout());

        // top: welcome + buttons
        JPanel top = new JPanel(new BorderLayout());
        JLabel header = new JLabel("WELCOME TO Z-U-A BANK", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        top.add(header, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(1, 6, 6, 6));
        JButton createBtn = new JButton("Create Account");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton txBtn = new JButton("Check Transaction");
        JButton detailsBtn = new JButton("Get Account Details");
        JButton exitBtn = new JButton("Exit");

        buttons.add(createBtn);
        buttons.add(depositBtn);
        buttons.add(withdrawBtn);
        buttons.add(txBtn);
        buttons.add(detailsBtn);
        buttons.add(exitBtn);

        top.add(buttons, BorderLayout.CENTER);
        mainPanel.add(top, BorderLayout.NORTH);

        // center: output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(outputArea);
        mainPanel.add(scroll, BorderLayout.CENTER);

        // button actions
        createBtn.addActionListener(_ -> createAccountDialog());
        depositBtn.addActionListener(_ -> depositDialog());
        withdrawBtn.addActionListener(_ -> withdrawDialog());
        txBtn.addActionListener(_ -> checkTransactionDialog());
        detailsBtn.addActionListener(_ -> getAccountDetailsDialog());
        exitBtn.addActionListener(_ -> {
            int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) System.exit(0);
        });
    }

    // ---------------- Dialogs ----------------
    private void createAccountDialog() {
        JTextField nameF = new JTextField(20);
        JTextField cnicF = new JTextField(20);
        JTextField contactF = new JTextField(20);
        JTextField emailF = new JTextField(20);
        JTextField addressF = new JTextField(20);
        JPasswordField pinF = new JPasswordField(20);

        JPanel p = new JPanel(new GridLayout(0,2,6,6));
        p.add(new JLabel("Name:")); p.add(nameF);
        p.add(new JLabel("CNIC:")); p.add(cnicF);
        p.add(new JLabel("Contact:")); p.add(contactF);
        p.add(new JLabel("Email:")); p.add(emailF);
        p.add(new JLabel("Address:")); p.add(addressF);
        p.add(new JLabel("4-digit PIN:")); p.add(pinF);

        int result = JOptionPane.showConfirmDialog(this, p, "Create Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameF.getText().trim();
            String cnic = cnicF.getText().trim();
            String contact = contactF.getText().trim();
            String email = emailF.getText().trim();
            String address = addressF.getText().trim();
            String pinS = new String(pinF.getPassword()).trim();

            if (name.isEmpty() || pinS.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and PIN are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int pin;
            try { pin = Integer.parseInt(pinS); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "PIN must be numeric.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            String accNum = Generate_Account_Number();
            double balance = 0.0;

            try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
                fw.write("Account Holder Name: " + name + "\n");
                fw.write("Account Holder CNIC: " + cnic + "\n");
                fw.write("Account Holder Contact: " + contact + "\n");
                fw.write("Account Holder Email: " + email + "\n");
                fw.write("Account Holder Address: " + address + "\n");
                fw.write("Account Holder PIN: " + pin + "\n");
                fw.write("Account Number: " + accNum + "\n");
                fw.write("Balance: " + balance + "\n");
                fw.write("============================\n");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            outputArea.append("Account Created Successfully!\nAccount Number: " + accNum + "\n\n");
        }
    }

    private void depositDialog() {
        String acc = JOptionPane.showInputDialog(this, "Enter Account Number:");
        if (acc == null || acc.trim().isEmpty()) return;
        String amtS = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (amtS == null) return;
        double amount;
        try { amount = Double.parseDouble(amtS); }
        catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE); return; }

        if (amount <= 0) { JOptionPane.showMessageDialog(this, "Amount must be > 0.", "Error", JOptionPane.ERROR_MESSAGE); return; }

        if (Update_Balance(acc.trim(), amount, true)) {
            Log_Transaction(acc.trim(), "Deposit", amount);
            outputArea.append("Deposited " + amount + " to Account " + acc + "\n\n");
        } else {
            JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void withdrawDialog() {
        String acc = JOptionPane.showInputDialog(this, "Enter Account Number:");
        if (acc == null || acc.trim().isEmpty()) return;
        String amtS = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (amtS == null) return;
        double amount;
        try { amount = Double.parseDouble(amtS); }
        catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE); return; }

        if (amount <= 0) { JOptionPane.showMessageDialog(this, "Amount must be > 0.", "Error", JOptionPane.ERROR_MESSAGE); return; }

        if (Update_Balance(acc.trim(), amount, false)) {
            Log_Transaction(acc.trim(), "Withdraw", amount);
            outputArea.append("Withdrawn " + amount + " from Account " + acc + "\n\n");
        } else {
            JOptionPane.showMessageDialog(this, "Account not found or insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkTransactionDialog() {
        String acc = JOptionPane.showInputDialog(this, "Enter Account Number:");
        if (acc == null || acc.trim().isEmpty()) return;
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction History for Account: ").append(acc.trim()).append("\n");
        sb.append("----------------------------------------\n");

        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Account Number: ") && line.contains(acc.trim())) {
                    found = true;
                    sb.append(line).append('\n');
                    for (int i = 0; i < 3; i++) {
                        line = reader.readLine();
                        if (line == null || line.startsWith("========================================")) break;
                        sb.append(line).append('\n');
                    }
                    sb.append("----------------------------------------\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading transaction file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!found) sb.append("No transaction history found for this account.\n");

        outputArea.append(sb.toString() + "\n");
    }

    private void getAccountDetailsDialog() {
        String acc = JOptionPane.showInputDialog(this, "Enter Account Number:");
        if (acc == null || acc.trim().isEmpty()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean found = false;
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Account Number: ") && line.contains(acc.trim())) {
                    found = true;
                    sb.append("--- Account Details ---\n");
                    for (int i = 0; i < 7; i++) {
                        sb.append(line).append('\n');
                        line = reader.readLine();
                        if (line == null || line.startsWith("============================")) break;
                    }
                    break;
                }
            }

            if (!found) sb.append("Account not found.\n");
            outputArea.append(sb.toString() + "\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading account details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- Backend logic (ported from your CLI) ----------------
    private void Initialize_Login_File() {
        File loginFile = new File(LOGIN_FILE);
        if (!loginFile.exists()) {
            try (FileWriter fw = new FileWriter(LOGIN_FILE)) {
                fw.write("admin\n");
                fw.write("admin123\n");
                System.out.println("Default login credentials created: admin/admin123");
            } catch (IOException e) {
                System.err.println("Error creating login file: " + e.getMessage());
            }
        }
    }

    private boolean Validate_Credentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(LOGIN_FILE))) {
            String storedUsername = br.readLine();
            String storedPassword = br.readLine();
            return username.equals(storedUsername) && password.equals(storedPassword);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading login file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void ensureDataFilesExist() {
        try {
            new File(FILE_NAME).createNewFile();
            new File(TRANSACTION_FILE).createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating data files: " + e.getMessage());
        }
    }

    private String Generate_Account_Number() {
        Random random = new Random();
        String accNum;
        do {
            accNum = "PK-" + (100000 + random.nextInt(900000));
        } while (Account_Exists(accNum));
        return accNum;
    }

    private boolean Account_Exists(String accNum) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Account Number: ") && line.contains(accNum)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    private boolean Update_Balance(String accNum, double amount, boolean isDeposit) {
        try {
            List<String> lines = new ArrayList<>();
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Account Number: ") && line.contains(accNum)) {
                        found = true;
                        lines.add(line); // account number line
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
            System.err.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }

    private void Log_Transaction(String accNum, String type, double amount) {
        try (FileWriter fw = new FileWriter(TRANSACTION_FILE, true)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            fw.write("Account Number: " + accNum + "\n");
            fw.write("Type: " + type + "\n");
            fw.write("Amount: " + amount + "\n");
            fw.write("Date: " + timestamp + "\n");
            fw.write("========================================\n");
        } catch (IOException e) {
            System.err.println("Error writing transaction: " + e.getMessage());
        }
    }

    // ---------------- Main ----------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            ImageIcon icon = new ImageIcon("logo.png");
            app.setIconImage(icon.getImage());
            app.setVisible(true);
        });
    }
}
