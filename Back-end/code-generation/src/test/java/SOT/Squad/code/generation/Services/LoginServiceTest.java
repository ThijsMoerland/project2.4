package SOT.Squad.code.generation.Services;

import SOT.Squad.code.generation.JWT.JWTTokenProvider;
import SOT.Squad.code.generation.Models.DTO.LoginRequestDTO;
import SOT.Squad.code.generation.Models.DTO.LoginResponseDTO;
import SOT.Squad.code.generation.Models.User;
import SOT.Squad.code.generation.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private JWTTokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnLoginResponseDTO() {
        String username = "testuser";
        String password = "testpassword";
        String encodedPassword = "encodedpassword";
        String token = "generatedtoken";

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setUsername(username);
        requestDTO.setPassword(password); // Use the actual password instead of encodedPassword

        LoginResponseDTO expectedResponseDTO = new LoginResponseDTO(token);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(encoder.matches(password, encodedPassword)).thenReturn(true);
        when(tokenProvider.createToken(username, user.getRoles())).thenReturn(token);

        LoginResponseDTO responseDTO = loginService.login(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(expectedResponseDTO.getToken(), responseDTO.getToken());
        verify(userRepository, times(1)).findUserByUsername(username);
        verify(encoder, times(1)).matches(password, encodedPassword);
        verify(tokenProvider, times(1)).createToken(username, user.getRoles());
    }


    @Test
    void login_WithInvalidUsername_ShouldThrowIllegalArgumentException() {
        String username = "testusername";
        String password = "testpassword";
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setUsername(username);
        requestDTO.setPassword(password);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> loginService.login(requestDTO));
        verify(userRepository, times(1)).findUserByUsername(username);
        verify(encoder, never()).matches(anyString(), anyString());
        verify(tokenProvider, never()).createToken(anyString(), any());
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowIllegalArgumentException() {
        String username = "testuser";
        String password = "testpassword";
        String encodedPassword = "encodedpassword";

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        LoginRequestDTO requestDTO = new LoginRequestDTO();

        requestDTO.setUsername(username);
        requestDTO.setPassword(password);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(encoder.matches(password, encodedPassword)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> loginService.login(requestDTO));
        verify(userRepository, times(1)).findUserByUsername(username);
        verify(encoder, times(1)).matches(password, encodedPassword);
        verify(tokenProvider, never()).createToken(anyString(), any());
    }
}
