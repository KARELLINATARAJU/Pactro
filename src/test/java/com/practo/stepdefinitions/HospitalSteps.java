package com.practo.stepdefinitions;

import io.cucumber.java.en.*;
import com.practo.pages.PractoHospitalScraper;
import com.practo.utils.Hooks;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class HospitalSteps {

    WebDriver driver = Hooks.getDriver();
    PractoHospitalScraper hp = new PractoHospitalScraper(driver);

    @Given("I open the Practo website")
    public void openPracto() {
        hp.openPage();
        ExtentCucumberAdapter.addTestStepLog("Opened Practo homepage.");
    }

    @When("I search for hospitals in {string}")
    public void searchHospitals(String city) {
        hp.search(city);
        ExtentCucumberAdapter.addTestStepLog("Searched for hospitals in: " + city);
    }

    @And("I filter hospitals that offer \"24x7\" service and rating greater than 3.5")
    public void filterHospitals() {
        hp.filterHospitals();
        ExtentCucumberAdapter.addTestStepLog("Filtered hospitals with 24x7 service and rating > 3.5.");
    }

    @Then("I print the filtered hospital list")
    public void printHospitalsWithRating() {
        hp.printHospitalsWithRating();
        ExtentCucumberAdapter.addTestStepLog("Printed filtered hospital list.");
    }
}
