package SOT.Squad.code.generation.Repositories;

import SOT.Squad.code.generation.Models.AccountType;
import SOT.Squad.code.generation.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBankAccountFromOrBankAccountTo(String bankAccountFrom, String bankAccountTo);
    List<Transaction> findByBankAccountToAndAccountTypeToInOrBankAccountFromAndAccountTypeFromIn(
            String bankAccountTo, List<AccountType> accountTypeTo, String bankAccountFrom, List<AccountType> accountTypeFrom);
}
