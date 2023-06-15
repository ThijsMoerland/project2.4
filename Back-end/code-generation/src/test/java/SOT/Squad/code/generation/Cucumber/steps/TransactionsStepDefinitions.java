package SOT.Squad.code.generation.Cucumber.steps;


import SOT.Squad.code.generation.Models.*;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
public class TransactionsStepDefinitions{
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private TestRestTemplate restTemplate  = new TestRestTemplate();
    private ResponseEntity<String> response;
    private String token;
    private User user;
    private BankAccount bankAccount;
    private Transaction updatedTransaction;
    private BankAccount updatedBankAccount;
    private Transaction retrievedTransaction;
    private int transactionId;
    private List<Transaction> retrievedTransactions;




    @Given("I am logged in as username {string} with password {string}")
    public void iAmLoggedInAsUsernameWithPassword(String arg0, String arg1) throws Throwable {
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate
                .exchange("http://localhost:8080/" + "login",
                        HttpMethod.POST,
                        new HttpEntity<>("{\"username\":\"" + arg0 + "\", \"password\":\"" + arg1 + "\"}", httpHeaders), // null because OPTIONS does not have a body
                        String.class);

        token = JsonPath.read(response.getBody(), "$.token");
        httpHeaders.add("Authorization", "Bearer " + token);
    }

    @Given("The endpoint for transactions is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String method) throws Throwable {
        response = restTemplate
                .exchange("http://localhost:8080/" + "transactions",
                        HttpMethod.OPTIONS,
                        new HttpEntity<>(null, httpHeaders), // null because OPTIONS does not have a body
                        String.class);

        List<String> options = Arrays.stream(response.getHeaders()
                        .get("Allow")
                        .get(0)// The first element is all allowed methods separated by comma
                        .split(","))
                .toList();
        Assertions.assertTrue(options.contains(method));
    }
    @When("the transaction is added")
    public void theTransactionIsAdded() {
        bankAccount = new BankAccount(1, "NL12INHO0123456789",   1000, 1, false, "EUR", List.of(AccountType.CURRENT),10);
        user = new User(1, "thijs", "moerland", "Thijs", "Moerland", 64567, "Moerland8", "123street", 53, "2131GB", "hoofddorp",
                List.of(bankAccount.getId()), true, List.of(Role.CUSTOMER), "5781", 2000, 300);
        Transaction transaction = new Transaction(1, "test", 100, "NL12INHO0123456789", "NL12INHO0123456788",
                List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "kenmerk",
                LocalDateTime.now().minusDays(3), user);

        response = restTemplate.exchange("http://localhost:8080/transactions",
                HttpMethod.POST,
                new HttpEntity<>(transaction, httpHeaders),
                String.class);
    }

    @Then("the transaction is successfully created")
    public void theTransactionIsSuccessfullyCreated() {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Given("a valid transaction ID")
    public void aValidTransactionID() {
        transactionId = 1;
    }
    @And("The endpoint for transactions :id is available for method {string}")
    public void theEndpointForTransactionsIdIsAvailableForMethod(String method) {
        String endpoint = "http://localhost:8080/transactions/" + transactionId;
        response = restTemplate
                .exchange(endpoint,
                        HttpMethod.OPTIONS,
                        new HttpEntity<>(null, httpHeaders),
                        String.class);

        List<String> options = Arrays.asList(response.getHeaders().get("Allow").get(0).split(","));
        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }


    @When("the transaction is retrieved")
    public void theTransactionIsRetrieved() {
        String endpoint = "http://localhost:8080/transactions/" + transactionId;

        ResponseEntity<Transaction> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        retrievedTransaction = responseEntity.getBody();
    }

    @Then("the transaction details are returned")
    public void theTransactionDetailsAreReturned() {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(retrievedTransaction);
    }

    @And("updated transaction details")
    public void updatedTransactionDetails() {
        bankAccount = new BankAccount(2, "NL12INHO0123456789", 2000, 1, false, "EUR", List.of(AccountType.CURRENT), 10);
        Transaction updatedTransaction = new Transaction(1, "updated test", 200, "NL12INHO0123456789", "NL12INHO0123456788",
                List.of(AccountType.CURRENT), List.of(AccountType.CURRENT), "updated kenmerk",
                LocalDateTime.now().minusDays(2), user);


        retrievedTransaction = updatedTransaction;
    }

    @When("the transaction is updated")
    public void theTransactionIsUpdated() {
        String endpoint = "http://localhost:8080/transactions/" + transactionId;
        Transaction updatedTransaction = retrievedTransaction; // Set the updated transaction details here

        response = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                new HttpEntity<>(updatedTransaction, httpHeaders),
                String.class
        );
    }

    @Then("the transaction is successfully modified")
    public void theTransactionIsSuccessfullyModified() {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @When("I retrieve all transactions")
    public void iRetrieveAllTransactions() {
        String endpoint = "http://localhost:8080/transactions";

        ResponseEntity<List<Transaction>> responseEntity = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                new ParameterizedTypeReference<>() {}
        );

        List<Transaction> transactions = responseEntity.getBody();
        retrievedTransactions = transactions;
    }

    @Then("I should receive all transactions")
    public void iShouldReceiveAllTransactions() {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(retrievedTransactions);
        Assertions.assertFalse(retrievedTransactions.isEmpty());
    }
}
