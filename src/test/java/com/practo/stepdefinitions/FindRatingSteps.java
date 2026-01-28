package com.practo.stepdefinitions;

import io.cucumber.java.en.*;
import com.practo.pages.RatingFinder;
import com.practo.utils.Hooks;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class FindRatingSteps {

    private final WebDriver driver;
    private final RatingFinder rf;

    public FindRatingSteps() {
        this.driver = Hooks.getDriver();
        if (this.driver == null) {
            throw new IllegalStateException("WebDriver is null. Ensure Hooks initialized the driver before step execution.");
        }
        this.rf = new RatingFinder(driver);
    }

    @Given("I open the website")
    public void open() {
        rf.open();
        ExtentCucumberAdapter.addTestStepLog("Opened Practo homepage.");
    }

    @When("I search for hospital in {string}")
    public void i_check_for_rating_in_coimbatore(String city) {
        rf.searchhospital(city);
        ExtentCucumberAdapter.addTestStepLog("Searched for hospitals in: " + city);
    }

    @Then("I check for rating above {double} in {string}")
    public void checkRating(Double ratingThreshold, String city) {
        rf.findrating(city, ratingThreshold);
        ExtentCucumberAdapter.addTestStepLog(
                String.format("Validated rating ≥ %.2f in %s", ratingThreshold, city)
        );
    }

    @Then("I print the result")
    public void printRatingResult() {
        rf.result();
        ExtentCucumberAdapter.addTestStepLog("Printed rating result.");
    }
}
