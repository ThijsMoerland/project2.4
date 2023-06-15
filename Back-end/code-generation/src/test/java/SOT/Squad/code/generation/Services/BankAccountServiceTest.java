package SOT.Squad.code.generation.Services;

import SOT.Squad.code.generation.Models.AccountType;
import SOT.Squad.code.generation.Models.BankAccount;
import SOT.Squad.code.generation.Models.DTO.IbanAndNameDTO;
import SOT.Squad.code.generation.Models.User;
import SOT.Squad.code.generation.Repositories.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;



import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BankAccountServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBankAccounts_shouldReturnAllBankAccounts() {
        List<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(new BankAccount());
        bankAccounts.add(new BankAccount());

        when(bankAccountRepository.findAll()).thenReturn(bankAccounts);
        List<BankAccount> result = bankAccountService.getAllBankAccounts();

        assertEquals(2, result.size());
    }

    @Test
    void addBankAccount_shouldSaveBankAccount() {
        BankAccount bankAccount = new BankAccount();

        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
        BankAccount result = bankAccountService.addBankAccount(bankAccount);

        assertEquals(bankAccount, result);
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    void updateBankAccount_shouldUpdateBankAccount() {
        BankAccount bankAccount = new BankAccount();
        long id = 1L;

        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
        BankAccount result = bankAccountService.updateBankAccount(bankAccount, id);

        assertEquals(bankAccount, result);
        assertEquals(id, bankAccount.getId());
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    void deleteBankAccount_shouldDisableBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccountService.deleteBankAccount(bankAccount);

        assertTrue(bankAccount.isDisabled());
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    void getBankAccountById_shouldReturnBankAccount() {
        long id = 1L;
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(id);
        when(bankAccountRepository.getAllById(id)).thenReturn(bankAccount);

        BankAccount result = bankAccountService.getBankAccountById(id);
        assertEquals(bankAccount, result);
    }

    @Test
    void getAllNameAndIban_shouldReturnIbanAndNameDTOList() {
//        // Arrange
//        List<BankAccount> bankList = new ArrayList<>();
//        BankAccount bankAccount1 = new BankAccount();
//        bankAccount1.setId(1L);
//        bankAccount1.setIban("1234567890");
//        bankAccount1.setUserId(1L);
//        bankAccount1.setAccountType(List.of(AccountType.SAVINGS));
//        bankList.add(bankAccount1);
//
//        User user = new User();
//        user.setId(1L);
//        user.setFirstName("John");
//        user.setLastName("Doe");
//
//        when(bankAccountRepository.findAll()).thenReturn(bankList);
//        when(userService.getUser(bankAccount1.getUserId())).thenReturn(user);
//
//        // Act
//        List<IbanAndNameDTO> result = bankAccountService.getAllNameAndIban();
//
//        // Assert
//        assertEquals(1, result.size());
//        IbanAndNameDTO dto = result.get(0);
//        assertEquals(bankAccount1.getIban(), dto.getIban());
//        assertEquals(user.getFirstName() + " " + user.getLastName(), dto.getName());
//        assertEquals(bankAccount1.getAccountType(), dto.getAccountType());
//        assertEquals(bankAccount1.getId(), dto.getId());
    }
}
