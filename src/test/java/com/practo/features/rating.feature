Feature: Rating finder

  Scenario: Search Hospitals in Bangalore and find rating above 4.5
    Given I open the website
    When I search for hospital in "Bangalore"
    Then I check for rating above 4.5 in "Bangalore"
    Then I print the result
