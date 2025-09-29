 🏦  Bank Management System (Java OOP Project)

A **robust Bank Management System built in Java** demonstrating **Object-Oriented Programming principles** including **Abstraction, Inheritance, Polymorphism, and Encapsulation**. This console-based system simulates real-world banking operations with a **professional, interactive CLI**.


https://github.com/user-attachments/assets/2e4c9827-85ef-42c6-ba93-39ac2505bed2


---

## 🚀 Features

- ✅ **Savings & Current Accounts** with separate rules  
- ✅ **Deposit & Withdraw** securely with validation  
- ✅ **Overdraft limit** for Current Accounts  
- ✅ **Monthly interest** calculation for Savings Accounts  
- ✅ **Transaction history** with timestamps  
- ✅ **Add multiple accounts at once**  
- ✅ **Delete accounts** safely  
- ✅ **Persistent storage** using Java Serialization (`bank_data.ser`)  
- ✅ **User-friendly CLI** with menu-driven operations  

---

## 🛠 Technologies Used

- **Java 17+**  
- **OOP Concepts**: Abstraction, Encapsulation, Inheritance, Polymorphism  
- **File Handling & Serialization** for persistence  
- **Collections Framework** (`HashMap`, `ArrayList`)  
- **Exception Handling** for robust operation  

---

## 📸 Sample CLI Interaction

```
================= BANK MENU =================
1. Open Savings Account
2. Open Current Account
3. Deposit
4. Withdraw
5. Check Balance
6. Transaction History
7. Apply Interest to Savings
8. Show All Accounts
9. Open Multiple Accounts
10. Delete an Account
0. Exit
============================================
👉 Enter choice: 1
Holder Name: Vansh
Initial Balance: 10000
Interest Rate (%): 5
✅ Savings Account Created: ACC1001

👉 Enter choice: 3
Account Number: ACC1001
Deposit Amount: 5000
✅ Deposit successful. New Balance: ₹15,000.00
```

---

## 🎯 Learning Outcomes

* Apply **Java OOP concepts** in a real-world simulation
* Implement **banking operations** with validations
* Use **file persistence** to store data between sessions
* Build **interactive CLI applications** for professional projects

---

## 🔗 Live Working

Although this is a **console application**, you can **run it locally** to experience full functionality:

1. Create accounts (single or multiple)
2. Deposit / Withdraw money
3. View transaction history
4. Apply monthly interest
5. Delete accounts
6. All data persists across sessions

---

## 📂 Repository Structure

```
bank-management-system/
│
├─ BankSystem.java     # Main application file
├─ bank_data.ser          # Serialized account data (auto-created)
└─ README.md
```

---

## 💡 Notes

* Make sure **Java 17+** is installed.
* All **data is automatically saved** to `bank_data.ser`.
* Supports **multiple users and batch account creation**.
* Professional-grade CLI output for clean, readable interaction.

---
