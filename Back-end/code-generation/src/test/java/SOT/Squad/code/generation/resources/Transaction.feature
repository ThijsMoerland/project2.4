Feature: Transaction API

  Scenario: Add a transaction as EMPLOYEE
    Given I am logged in as username "serena" with password "kenter"
    And The endpoint for transactions is available for method "POST"
    When the transaction is added
    Then the transaction is successfully created

  Scenario: Add a transaction as CUSTOMER
    Given I am logged in as username "om" with password "al"
    And The endpoint for transactions is available for method "POST"
    When the transaction is added
    Then the transaction is successfully created

  Scenario: Retrieve a transaction by ID
    Given I am logged in as username "om" with password "al"
    Given a valid transaction ID
    And The endpoint for transactions :id is available for method "GET"
    When the transaction is retrieved
    Then the transaction details are returned

  Scenario: Update a transaction
    Given I am logged in as username "om" with password "al"
    Given a valid transaction ID
    And updated transaction details
    When the transaction is updated
    Then the transaction is successfully modified


  Scenario: Getting all transactions
    Given I am logged in as username "serena" with password "kenter"
    And The endpoint for transactions is available for method "GET"
    When I retrieve all transactions
    Then I should receive all transactions

