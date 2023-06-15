package SOT.Squad.code.generation.Controllers;

import SOT.Squad.code.generation.JWT.JWTTokenProvider;
import SOT.Squad.code.generation.Models.DTO.LoginResponseDTO;
import SOT.Squad.code.generation.Models.User;
import SOT.Squad.code.generation.Services.UserService;
import SOT.Squad.code.generation.JWT.JWTKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    JWTKeyProvider keyProvider;


    @Autowired
    JWTTokenProvider tokenProvider;

    @GetMapping() //Employee
    public List<User> getAllUsers() {
        try {
            keyProvider.decodeJWT();
            return userService.getAllUsers();

        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping //Employee
    public User addUser(@RequestBody User user) {
        try {
            keyProvider.decodeJWT();
            return userService.addUser(user);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/register")//Employee
    public User register(@RequestBody User user) {
        try {
            return userService.addUser(user);
        }catch (Exception e){
            return null;
        }
    }

    @GetMapping("/{id}") //Employee & Customer
    public User getUser(@PathVariable long id) {
        try {
            keyProvider.decodeJWT();

            return userService.getUser(id);
        }catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/current") //Employee & Customer
    public User getUserOnUsername() {
        try {
            String username = keyProvider.decodeJWT();
            return userService.getUserByUsername(username);
        }catch (Exception e) {
            return null;
        }
    }

    @PutMapping("/{id}") //Employee & Customer
    public User updateUser(@PathVariable long id, @RequestBody User user) {
        try {
            keyProvider.decodeJWT();
            user.setId(id);
            return userService.updateUser(user);
        }catch (Exception e) {
            return null;
        }

    }

    @GetMapping("/pincode/{pincode}") //Employee & Customer
    public User checkPincode(@PathVariable String pincode) {
        try {
            keyProvider.decodeJWT();
            return userService.checkPincode(pincode);
        }catch (Exception e) {
            return null;
        }


    }

}
