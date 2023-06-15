package SOT.Squad.code.generation.Controllers;

import SOT.Squad.code.generation.JWT.JWTKeyProvider;
import SOT.Squad.code.generation.Models.*;
import SOT.Squad.code.generation.Models.DTO.IbanAndNameDTO;
import SOT.Squad.code.generation.Services.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class BankAccountRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankAccountRestController bankAccountRestController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bankAccountRestController).build();
        objectMapper = new ObjectMapper();

        // Mock the JWTKeyProvider using @MockBean
        JWTKeyProvider keyProviderMock = Mockito.mock(JWTKeyProvider.class);
        Mockito.when(keyProviderMock.decodeJWT()).thenReturn("mockedToken");

        // Inject the mocked JWTKeyProvider into the controller using @MockBean
        ReflectionTestUtils.setField(bankAccountRestController, "keyProvider", keyProviderMock);
    }

    @Test
    public void testAddBankAccount() throws Exception {
        BankAccount bankAccount = new BankAccount();
        when(bankAccountService.addBankAccount(any(BankAccount.class))).thenReturn(bankAccount);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bankaccounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankAccount)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void testGetAccountById() throws Exception {
        long accountId = 1;
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(accountId);
        when(bankAccountService.getBankAccountById(accountId)).thenReturn(bankAccount);

        mockMvc.perform(get("/bankaccounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accountId));

        verify(bankAccountService, times(1)).getBankAccountById(accountId);
    }

    @Test
    void testGetAllBankAccounts() throws Exception {
        List<BankAccount> bankAccounts = new ArrayList<>();
        when(bankAccountService.getAllBankAccounts()).thenReturn(bankAccounts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bankaccounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteBankAccount() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        bankAccount.setDisabled(false);

        doNothing().when(bankAccountService).deleteBankAccount(bankAccount);

        mockMvc.perform(MockMvcRequestBuilders.put("/bankaccounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"disabled\": false}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));

        verify(bankAccountService, times(1)).deleteBankAccount(bankAccount);
    }


    @Test
    public void testUpdateBankAccount() throws Exception {
        BankAccount bankAccount = new BankAccount();
        long id = 2;
        bankAccount.setId(id);
        bankAccount.setBalance(1000);
        bankAccount.setIban("NL12INHO0123456789");
        bankAccount.setUserId(2);
        bankAccount.setAbsoluutLimit(5000);
        bankAccount.setAccountType(List.of(AccountType.CURRENT));
        bankAccount.setCurrencies("USD");

        when(bankAccountService.updateBankAccount(any(BankAccount.class), anyLong())).thenReturn(bankAccount);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bankaccounts/change/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2, \"disabled\": false, \"balance\": 1000, " +
                                "\"iban\": \"NL12INHO0123456789\", \"userId\": 2, \"absoluutLimit\": 5000, " +
                                "\"accountType\": [\"CURRENT\"], \"currencies\": \"USD\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

        verify(bankAccountService, times(1)).updateBankAccount(any(BankAccount.class), eq(id));
    }


    @Test
    public void testGetAllNameAndIban() throws Exception {
        // Mocking the response from bankAccountService.getAllNameAndIban()
        List<IbanAndNameDTO> mockList = new ArrayList<>();
        // Add sample data to the mock list
        IbanAndNameDTO dto1 = new IbanAndNameDTO();
        dto1.setId(1L);
        dto1.setName("John Doe");
        dto1.setIban("IBAN123");
        mockList.add(dto1);

        IbanAndNameDTO dto2 = new IbanAndNameDTO();
        dto2.setId(2L);
        dto2.setName("Jane Smith");
        dto2.setIban("IBAN456");
        mockList.add(dto2);

        when(bankAccountService.getAllNameAndIban()).thenReturn(mockList);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bankaccounts/All")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].iban").value("IBAN123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].iban").value("IBAN456"));
    }
}
