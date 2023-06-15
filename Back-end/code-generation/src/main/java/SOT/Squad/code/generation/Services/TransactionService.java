package SOT.Squad.code.generation.Services;


import SOT.Squad.code.generation.Models.AccountType;
import SOT.Squad.code.generation.Models.Transaction;
import SOT.Squad.code.generation.Repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    private List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> GetAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public Transaction GetTransactionById(long id) {
        return transactionRepository.findById(id).get();
    }
    public List<Transaction> findByBankAccountAndAccountType(String iban, List<AccountType> accountType) {
        return transactionRepository.findByBankAccountToAndAccountTypeToInOrBankAccountFromAndAccountTypeFromIn(iban, accountType, iban, accountType);
    }
    public List<Transaction> GetTransactionsByIban(String iban) {
        return transactionRepository.findByBankAccountFromOrBankAccountTo(iban, iban);
    }
    public Transaction AddTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction UpdateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
