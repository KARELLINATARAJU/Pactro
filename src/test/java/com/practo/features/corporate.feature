Feature: Corporate Wellness Form

  Scenario: Fill invalid form and capture errors
    Given I open the Corporate Wellness form
    When I fill invalid details and submit
    Then I should see validation error messages
