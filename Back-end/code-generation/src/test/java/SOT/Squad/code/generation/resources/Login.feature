Feature: Bank Account Rest Controller

  Scenario: Login with valid credentials
    Given I have valid login credentials username "serena" and password "kenter"
    And The endpoint for "login" is available for the method "POST"
    When I submit my login credentials username "serena" and password "kenter"
    Then the response should be a successful login response with a token

  Scenario: Login with invalid credentials
    Given I have invalid login credentials username "test" and password "test"
    And The endpoint for "login" is available for the method "POST"
    When I submit my login credentials username "test" and password "test"
    Then the response should be an error indicating invalid credentials

  Scenario: Login with missing credentials
    Given I have missing login credentials
    And The endpoint for "login" is available for the method "POST"
    When I submit without filling in my login credentials
    Then the response should be an error indicating missing credentials
