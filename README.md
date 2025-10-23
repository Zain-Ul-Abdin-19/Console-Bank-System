# 💰 Z-U-A Bank — Java Console Banking System

A simple **banking management system** built in **Java** using basic file handling (no databases).
This project simulates real-world banking operations such as creating accounts, deposits, withdrawals, and viewing transaction history — all via a command-line interface.

---

## 🧠 Overview

This project demonstrates how file I/O, exception handling, and modular programming can be used to implement core banking operations securely and efficiently.
It’s perfect for **BS Software Engineering** students learning Java fundamentals, object-oriented design, and persistent data storage using text files.

---

## ⚙️ Features

✅ **Create Account** – Register a new user and generate a unique account number.
✅ **Deposit Money** – Add funds to an existing account.
✅ **Withdraw Money** – Withdraw funds while checking for sufficient balance.
✅ **Transaction History** – View all deposits and withdrawals with timestamps.
✅ **Account Details** – Retrieve user details and current balance.
✅ **File Storage** – User data and transactions are stored in plain text files.
✅ **Error Handling** – Handles invalid inputs and file errors gracefully.

---

## 🧩 Technologies Used

* **Language:** Java (JDK 17 or later recommended)
* **Libraries:**

  * `java.io` – for file handling
  * `java.text.SimpleDateFormat` – for date formatting
  * `java.util` – for utilities like Random, Scanner, List, etc.

---

## 🗂️ File Structure

```
📁 ZUABank/
│
├── Main.java                  # Main program file
├── User_Data.txt              # Stores account information
├── Transaction_History.txt    # Stores transaction logs
└── README.md                  # Documentation
```

---

## 🚀 How to Run

1. **Clone this repository**

   ```bash
   git clone https://github.com/your-username/ZUA-Bank.git
   cd ZUA-Bank
   ```

2. **Compile the program**

   ```bash
   javac Main.java
   ```

3. **Run the program**

   ```bash
   java Main
   ```

---

## 🧾 Sample Output

```
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

Enter your choice: 1
--- Creating Account ---
Enter Your Name: Zain
Enter Your CNIC: 00000-0000000-0
Enter Your Contact Number: 00000000000
Enter Your Email: zain@example.com
Enter Your Address: Pakistan
Set Your 4-digit PIN: 1234

Account Created Successfully!
Your Account Number is: PK-546321
```

---

## 🛡️ Important Notes

* All user data is stored in `.txt` files (no database required).
* The program uses **PINs** for basic user verification (you can extend this for login functionality).
* Always run this program in a **trusted local environment** to avoid exposing sensitive data.

---

## 🧠 Future Enhancements

* 🔐 Add account login authentication using PINs.
* 💳 Implement account transfer functionality.
* 📊 Use JSON or databases (like SQLite or MySQL) for data management.
* 🖥️ Develop a GUI (Swing or JavaFX).
* 🌐 Convert to a RESTful API with Spring Boot for web integration.

---

## 👨‍💻 Author

**Zain-ul-Abdeen**

---
