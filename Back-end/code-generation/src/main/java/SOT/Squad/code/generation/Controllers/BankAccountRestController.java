package SOT.Squad.code.generation.Controllers;

import SOT.Squad.code.generation.JWT.JWTKeyProvider;
import SOT.Squad.code.generation.Models.BankAccount;
import SOT.Squad.code.generation.Models.DTO.IbanAndNameDTO;
import SOT.Squad.code.generation.Services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bankaccounts")
public class BankAccountRestController {

    @Autowired
    JWTKeyProvider keyProvider;

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping //Employee & Customer
    public BankAccount addBankAccount(@RequestBody BankAccount bankAccount) {
        try {
            keyProvider.decodeJWT();

            return bankAccountService.addBankAccount(bankAccount);
        }catch (Exception e) {
            return null;
        }

    }

    @GetMapping("/{id}") //Employee & Customer
    public BankAccount getAccountById(@PathVariable long id) {
        try {
            keyProvider.decodeJWT();
            return bankAccountService.getBankAccountById(id);
        }catch (Exception e){
            return null;
        }

    }

    @GetMapping //Employee
    public List<BankAccount> getAllBankAccounts() {
        try {
            keyProvider.decodeJWT();

            return bankAccountService.getAllBankAccounts();
        }catch (Exception e) {
            return null;
        }

    }

    @PutMapping //Employee
    public boolean deleteBankAccount(@RequestBody BankAccount bankAccount) {
        try {
            keyProvider.decodeJWT();
            bankAccountService.deleteBankAccount(bankAccount);
            return true;
        }catch (Exception e) {
            return false;
        }

    }

    @PutMapping("/change/{id}") //Employee & Customer
    public BankAccount updateBankAccount(@PathVariable long id,@RequestBody BankAccount bankAccount) {
        try {
            keyProvider.decodeJWT();
            return bankAccountService.updateBankAccount(bankAccount, id);
        }catch (Exception e) {
            return null;
        }

    }
    @GetMapping("/userID/{id}")
    public List<BankAccount> getAllBankAccountsByUserId(@PathVariable long id) {
        try {
            keyProvider.decodeJWT();
            return bankAccountService.getAllBankAccountsByUserId(id);
        }catch (Exception e) {
            return null;
        }

    }


    @GetMapping("/All") //Employee
    public List<IbanAndNameDTO> getAllNameAndIban() {
        try {
            keyProvider.decodeJWT();
            return bankAccountService.getAllNameAndIban();
        }catch (Exception e) {
            return null;
        }

    }

    @GetMapping("/iban/{iban}") //Employee & Customer
    public BankAccount getBankAccountByIban(@PathVariable String iban) {
        try {
            keyProvider.decodeJWT();
            return bankAccountService.getBankAccountByIban(iban);
        }catch (Exception e) {
            return null;
        }

    }

}
