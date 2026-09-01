package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankController {

    @Autowired
    private AccountRepository accountRepository;


    // CREATE ACCOUNT
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(
            @RequestParam("name") String name,
            @RequestParam("accountNumber") long accountNumber,
            @RequestParam("balance") double balance) {

        try {

            Account account = new Account();

            account.setName(name);
            account.setAccountNumber(accountNumber);
            account.setBalance(balance);

            Account savedAccount = accountRepository.save(account);

            return ResponseEntity.ok(savedAccount);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body("Account creation failed: " + e.getMessage());
        }
    }


    // SHOW ALL ACCOUNTS
    @GetMapping("/accounts")
    public List<Account> getAccounts() {

        return accountRepository.findAll();
    }


    // CHECK BALANCE
    @GetMapping("/balance")
    public ResponseEntity<?> checkBalance(
            @RequestParam("id") int id) {

        Account account =
                accountRepository.findById(id).orElse(null);

        if (account == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Account not found!");

        }

        return ResponseEntity.ok(account);
    }


    // DEPOSIT MONEY
    @PutMapping("/deposit")
    public ResponseEntity<?> deposit(

            @RequestParam("id") int id,

            @RequestParam("amount") double amount) {

        Account account =
                accountRepository.findById(id).orElse(null);

        if (account == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Account not found!");

        }

        if (amount <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Amount must be greater than zero!");

        }

        account.setBalance(

                account.getBalance() + amount

        );

        Account updatedAccount =
                accountRepository.save(account);

        return ResponseEntity.ok(updatedAccount);
    }


    // WITHDRAW MONEY
    @PutMapping("/withdraw")
    public ResponseEntity<?> withdraw(

            @RequestParam("id") int id,

            @RequestParam("amount") double amount) {

        Account account =
                accountRepository.findById(id).orElse(null);

        if (account == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Account not found!");

        }

        if (amount <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Amount must be greater than zero!");

        }

        if (amount > account.getBalance()) {

            return ResponseEntity
                    .badRequest()
                    .body("Insufficient Balance!");

        }

        account.setBalance(

                account.getBalance() - amount

        );

        Account updatedAccount =
                accountRepository.save(account);

        return ResponseEntity.ok(updatedAccount);
    }


    // DELETE ACCOUNT
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(

            @RequestParam("id") int id) {

        if (!accountRepository.existsById(id)) {

            return ResponseEntity
                    .badRequest()
                    .body("Account not found!");

        }

        accountRepository.deleteById(id);

        return ResponseEntity.ok(

                "Account deleted successfully!"

        );
    }

}