package SOT.Squad.code.generation.Controllers;

import SOT.Squad.code.generation.Controllers.UserRestController;
import SOT.Squad.code.generation.JWT.JWTKeyProvider;
import SOT.Squad.code.generation.Models.*;
import SOT.Squad.code.generation.Repositories.UserRepository;
import SOT.Squad.code.generation.Services.BankAccountService;
import SOT.Squad.code.generation.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRestControllerTest {



    private ObjectMapper objectMapper;



    @InjectMocks
    private UserRestController userRestController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();

        objectMapper = new ObjectMapper();
// Mock the JWTKeyProvider using @MockBean

        JWTKeyProvider keyProviderMock = Mockito.mock(JWTKeyProvider.class);


        Mockito.when(keyProviderMock.decodeJWT()).thenReturn("mockedToken");
// Inject the mocked JWTKeyProvider into the controller using @MockBean

        ReflectionTestUtils.setField(userRestController, "keyProvider", keyProviderMock);
    }


    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private BankAccountService bankAccountService;
    @MockBean
    private JWTKeyProvider keyProvider;

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
// Add some transactions to the list

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
// Add some transactions to the list

        when(userService.addUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }

    @Test
    public void testregister() throws Exception {
        User user = new User();
// Add some transactions to the list

        when(userService.addUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }



    @Test
    public void testGetUser() throws Exception {
        User user = new User();
        long id = 1L;
// Add some transactions to the list

        when(userService.getUser(any(long.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void testGetUserOnUsername() throws Exception {
        User user = new User();
        JWTKeyProvider keyProviderMock = Mockito.mock(JWTKeyProvider.class);
        String username = keyProviderMock.decodeJWT();
// Add some transactions to the list

        when(userService.getUser(any(long.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        long id = 1L;
// Add some transactions to the list

        when(userService.updateUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }
    
    @Test
    public void testCheckPincode() throws Exception {
        User user = new User();
        String pincode = "4321";
        // Add some transactions to the list

        when(userService.checkPincode(any(String.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{pincode}", pincode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}