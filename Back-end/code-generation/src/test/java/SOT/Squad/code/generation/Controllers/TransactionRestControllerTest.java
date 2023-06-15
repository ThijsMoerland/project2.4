package SOT.Squad.code.generation.Controllers;

import SOT.Squad.code.generation.JWT.JWTKeyProvider;
import SOT.Squad.code.generation.Models.Role;
import SOT.Squad.code.generation.Models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import SOT.Squad.code.generation.Models.AccountType;
import SOT.Squad.code.generation.Models.Transaction;
import SOT.Squad.code.generation.Services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
class TransactionRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionRestController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();

        // Mock the JWTKeyProvider using @MockBean
        JWTKeyProvider keyProviderMock = Mockito.mock(JWTKeyProvider.class);
        Mockito.when(keyProviderMock.decodeJWT()).thenReturn("mockedToken");

        // Inject the mocked JWTKeyProvider into the controller using @MockBean
        ReflectionTestUtils.setField(transactionController, "keyProvider", keyProviderMock);

    }
    @Test
    void addTransaction() throws Exception {
        Transaction transaction = new Transaction();
        // Set up your transaction object

        when(transactionService.AddTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void getAllTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        // Add some transactions to the list

        when(transactionService.GetAllTransactions()).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void getTransactionsByIban() throws Exception {
        String iban = "NL12INHO0123456789";
        List<Transaction> transactions = new ArrayList<>();
        // Add some transactions to the list

        when(transactionService.GetTransactionsByIban(iban)).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/transactions/account/{iban}/{type}", iban, "CURRENT,SAVINGS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void getTransactionById() throws Exception {
        // Arrange
        long id = 1L;
        Transaction transaction = new Transaction();
        transaction.setId(id);
        // Set up the transaction with the given ID

        when(transactionService.GetTransactionById(id)).thenReturn(transaction);

        // Act and Assert
        mockMvc.perform(get("/transactions/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void findByBankAccountAndAccountType() throws Exception {
        String iban = "NL12INHO0123456789";
        List<AccountType> accountTypes = List.of(AccountType.CURRENT, AccountType.SAVINGS);
        List<Transaction> transactions = new ArrayList<>();

        User user1 = new User(1, "thijs", "moerland", "Thijs", "Moerland", 064567, "Moerland8", "123street", 53, "2131GB", "hoofddorp", null,true, List.of(Role.CUSTOMER), "5781",2000,300);
        Transaction transaction = new Transaction(1, "test", 100,  "NL12INHO0123456789", "NL12INHO0123456787", List.of(AccountType.CURRENT), List.of(AccountType.SAVINGS), "kenmerk", LocalDateTime.now(),user1);

        // Add some transactions to the list that match the provided IBAN and account types
        transactions.add(transaction);
        // Mock the service method call
        when(transactionService.findByBankAccountAndAccountType(iban, accountTypes)).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/transactions/account/{iban}/{type}", iban, accountTypes.stream().map(AccountType::toString).collect(Collectors.joining(",")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    void updateTransaction() throws Exception {
        long transactionId = 1L;
        Transaction updatedTransaction = new Transaction();

        when(transactionService.UpdateTransaction(any(Transaction.class))).thenReturn(updatedTransaction);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTransaction)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }
}