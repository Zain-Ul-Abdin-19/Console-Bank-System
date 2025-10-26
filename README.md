# 🏦 Z-U-A Bank Management System (Java Console Project)

A simple **Java-based Banking Management System** that simulates essential banking operations like account creation, deposits, withdrawals, and transaction history — all through a console interface.
This project uses **file handling** (`.txt` files) to manage user data, transactions, and owner login credentials, making it ideal for beginners learning about **Java I/O**, **control structures**, and **basic data persistence**.

---

## 🚀 Features

### 👤 Owner Login System

* Secure login authentication using stored credentials (`Owner_login.txt`).
* Default credentials auto-created if the file doesn't exist:

  ```
  Username: admin
  Password: admin123
  ```
* Limits login attempts to prevent unauthorized access.

### 💳 Account Management

* Create new customer accounts with personal information and unique account numbers.
* All account details are saved in `User_Data.txt`.

### 💰 Transactions

* **Deposit Funds:** Add money to an account.
* **Withdraw Funds:** Withdraw funds with balance checks.
* **Transaction Logging:** Every transaction is recorded in `Transaction_History.txt` with timestamps.

### 📜 Data Retrieval

* View account details by entering the account number.
* View complete transaction history for any account.

### 📁 File-Based Storage

All data is persisted in plain text files:

* `Owner_login.txt` → stores login credentials.
* `User_Data.txt` → stores user account information.
* `Transaction_History.txt` → stores logs of deposits and withdrawals.

---

## 🧠 Technologies Used

* **Language:** Java
* **Concepts:** File Handling, Exception Handling, OOP (Basic), Control Structures
* **IDE Recommended:** IntelliJ IDEA / Eclipse / VS Code with Java Extension Pack

---

## ⚙️ How to Run

### 1️⃣ Clone this repository

```bash
git clone https://github.com/your-username/ZUA-Bank-Management-System.git
```

### 2️⃣ Navigate to the project folder

```bash
cd ZUA-Bank-Management-System
```

### 3️⃣ Compile and Run the program

```bash
javac Main.java
java Main
```

---

## 🗂️ File Structure

```
ZUA-Bank-Management-System/
│
├── Main.java                  # Main source code
├── User_Data.txt              # Stores all account details
├── Transaction_History.txt    # Stores transaction logs
├── Owner_login.txt            # Stores owner login credentials
└── README.md                  # Documentation
```

---

## 📸 Sample Output

```
==============================
       OWNER LOGIN
==============================
Username: admin
Password: admin123
Login successful! Welcome, admin!

WELCOME TO Z-U-A Bank
==============================
          MAIN MENU
==============================
1: Create Account
2: Deposit
3: Withdraw
4: Check Transaction
5: Get Account Details
6: Exit
Enter your choice:
```

---

## 🧩 Future Enhancements

* Implement password encryption (e.g., using hashing).
* Add account deletion and update functionality.
* Integrate database (MySQL or SQLite) instead of text files.
* Create a GUI version using JavaFX or Swing.
* Add user login (separate from owner login).

## 👨‍💻 Author

**Zain-ul-Abdeen**
Student of BS Software Engineering
