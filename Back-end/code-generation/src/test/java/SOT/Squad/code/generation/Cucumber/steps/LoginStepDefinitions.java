package SOT.Squad.code.generation.Cucumber.steps;

import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;


public class LoginStepDefinitions {
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private String jwtToken;
    private ResponseEntity<String> responseEntity;
    private String uri = "http://localhost:8080/";


    @Given("I have valid login credentials username {string} and password {string}")
    public void iHaveValidLoginCredentialsUsernameAndPassword(String username, String password) {
        httpHeaders.add("Content-Type", "application/json");
        responseEntity = restTemplate
                .exchange(uri + "login",
                        HttpMethod.POST,
                        new HttpEntity<>("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}", httpHeaders), // null because OPTIONS does not have a body
                        String.class);

        jwtToken = JsonPath.read(responseEntity.getBody(), "$.token");
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
    }

    @When("I submit my login credentials username {string} and password {string}")
    public void iSubmitMyLoginCredentialsUsernameAndPassword(String username, String password) {
        iHaveValidLoginCredentialsUsernameAndPassword(username, password);
    }

    @Then("the response should be a successful login response with a token")
    public void theResponseShouldBeASuccessfulLoginResponseWithAToken() {
        assertNotNull(responseEntity);

        // Print the response status code and body for debugging purposes
        System.out.println("Response Status Code: " + responseEntity.getStatusCodeValue());
        System.out.println("Response Body: " + responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        // Assuming the response body is a JSON object containing a "token" field
        String token = JsonPath.read(responseEntity.getBody(), "$.token");
        assertNotNull(token);
    }

    @Given("I have invalid login credentials username {string} and password {string}")
    public void iHaveInvalidLoginCredentialsUsernameAndPassword(String username, String password) {
        httpHeaders.add("Content-Type", "application/json");

        // Create a request with invalid credentials
        String invalidCredentials = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        responseEntity = restTemplate.exchange(
                uri + "login",
                HttpMethod.POST,
                new HttpEntity<>(invalidCredentials, httpHeaders),
                String.class
        );
    }

    @Then("the response should be an error indicating invalid credentials")
    public void theResponseShouldBeAnErrorIndicatingInvalidCredentials() {
        assertNotNull(responseEntity);

        // Print the response status code and body for debugging purposes
        System.out.println("Response Status Code: " + responseEntity.getStatusCodeValue());
        System.out.println("Response Body: " + responseEntity.getBody());

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Given("I have missing login credentials")
    public void iHaveMissingLoginCredentials() {
        // No action needed, since we are intentionally submitting without credentials
    }

    @When("I submit without filling in my login credentials")
    public void iSubmitWithoutFillingInMyLoginCredentials() {
        httpHeaders.add("Content-Type", "application/json");

        // Create a request without any credentials
        responseEntity = restTemplate.exchange(
                uri + "login",
                HttpMethod.POST,
                new HttpEntity<>(httpHeaders),
                String.class
        );
    }

    @Then("the response should be an error indicating missing credentials")
    public void theResponseShouldBeAnErrorIndicatingMissingCredentials() {
        assertNotNull(responseEntity);

        // Print the response status code and body for debugging purposes
        System.out.println("Response Status Code: " + responseEntity.getStatusCodeValue());
        System.out.println("Response Body: " + responseEntity.getBody());

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

}
