Feature: Hospital Selection and Parking Facility Check

  Scenario: Select hospitals with 24x7 service and rating > 3.5, then check for parking
    Given I open the Practo website
    When I search for hospitals in "Bangalore"
    And I filter hospitals that offer "24x7" service and rating greater than 3.5
    Then I print the filtered hospital list
