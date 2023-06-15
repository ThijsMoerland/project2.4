package SOT.Squad.code.generation.configuration;

import SOT.Squad.code.generation.Models.*;
import SOT.Squad.code.generation.Services.BankAccountService;
import SOT.Squad.code.generation.Services.TransactionService;
import SOT.Squad.code.generation.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //Bankaccounts
        BankAccount bankacc1 = new BankAccount(1, "NL01INHO0000000001",   0, 0, false, "EUR", List.of(AccountType.CURRENT), 0);
        BankAccount bankacc2 = new BankAccount(2, "NL12INHO0123456789",   1000, 1, false, "EUR", List.of(AccountType.CURRENT),10);
        BankAccount bankacc3 = new BankAccount(3, "NL12INHO0123456788",  100, 2, false, "EUR", List.of(AccountType.CURRENT),10);
        BankAccount bankacc4 = new BankAccount(4, "NL12INHO0123456787",  100, 3, false, "EUR", List.of(AccountType.CURRENT),10);
        BankAccount bankacc5 = new BankAccount(5, "NL12INHO0123456787",  100, 3, false, "EUR", List.of(AccountType.SAVINGS),10);
        BankAccount bankacc6 = new BankAccount(6, "NL12INHO0123456787",  100, 3, false, "EUR", List.of(AccountType.SAVINGS),10);

        bankAccountService.addBankAccount(bankacc1);
        bankAccountService.addBankAccount(bankacc2);
        bankAccountService.addBankAccount(bankacc3);
        bankAccountService.addBankAccount(bankacc4);
        bankAccountService.addBankAccount(bankacc5);
        bankAccountService.addBankAccount(bankacc6);

        //Users
        User user1 = new User(1, "thijs", "moerland", "Thijs", "Moerland", 064567, "Moerland8", "123street", 53, "2131GB", "hoofddorp", List.of(bankacc2.getId()),true, List.of(Role.CUSTOMER), "5781",50,300);
        User user2 = new User(2, "om", "al", "Omar", "Al Sayasna", 064567, "Moerland8", "123street", 53, "2131GB", "hoofddorp",  List.of(bankacc3.getId()),true, List.of(Role.CUSTOMER), "2595",50,300);
        User user3 = new User(3, "serena", "kenter", "Serena", "Kenter", 064567, "Moerland8", "123street", 53, "2131GB", "hoofddorp", List.of(bankacc4.getId(), bankacc5.getId(), bankacc6.getId()),true, List.of(Role.EMPLOYEE), "3685",50,300);
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);

        //Transactions
        transactionService.AddTransaction(new Transaction(1, "test", 100,  "NL12INHO0123456789", "NL12INHO0123456788", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now().minusDays(3),user1));
        transactionService.AddTransaction(new Transaction(2, "test", 100,  "NL12INHO0123456789", "NL12INHO0123456787", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now().plusDays(4),user1));
        transactionService.AddTransaction(new Transaction(3, "test", 100,  "NL12INHO0123456789", "NL12INHO0123456788", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now().plusDays(4),user1));
        transactionService.AddTransaction(new Transaction(4, "test", 100,  "NL12INHO0123456788", "NL12INHO0123456787", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now().plusDays(4),user1));
        transactionService.AddTransaction(new Transaction(5, "test", 100,  "NL12INHO0123456788", "NL12INHO0123456789", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now().plusDays(4),user1));
        transactionService.AddTransaction(new Transaction(6, "test", 400,  "NL12INHO0123456788", "NL12INHO0123456787", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now(),user1));
        transactionService.AddTransaction(new Transaction(7, "test", 90,  "NL12INHO0123456787", "NL12INHO0123456788", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now(),user1));
        transactionService.AddTransaction(new Transaction(8, "test", 10,  "NL12INHO0123456787", "NL12INHO0123456789", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now(),user1));
        transactionService.AddTransaction(new Transaction(9, "test", 1.8,  "NL12INHO0123456787", "NL12INHO0123456788", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now(),user1));
        transactionService.AddTransaction(new Transaction(10, "test", 10.20,  "NL12INHO0123456787", "NL12INHO0123456789", List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk", LocalDateTime.now(),user1));
    }
}
