package com.practo.stepdefinitions;

import com.practo.pages.HomePage;
import com.practo.utils.Hooks;
import com.practo.pages.CorporateWellnessPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class CorporateSteps {
    CorporateWellnessPage wellnessPage;

    @Given("I open the Corporate Wellness form")
    public void i_open_the_corporate_wellness_form() {
        Hooks.getDriver().get("https://www.practo.com");
        ExtentCucumberAdapter.addTestStepLog("Opened Practo homepage.");

        HomePage homePage = new HomePage(Hooks.getDriver());
        homePage.goToCorporateWellness();
        ExtentCucumberAdapter.addTestStepLog("Navigated to Corporate Wellness page.");

        wellnessPage = new CorporateWellnessPage(Hooks.getDriver());
    }

    @When("I fill invalid details and submit")
    public void i_fill_invalid_details_and_submit() {
        wellnessPage.fillFormWithInvalidData();
        ExtentCucumberAdapter.addTestStepLog("Filled form with invalid data.");

        wellnessPage.selectDropdowns();
        ExtentCucumberAdapter.addTestStepLog("Selected dropdown values.");

        wellnessPage.triggerValidationManually();
        ExtentCucumberAdapter.addTestStepLog("Triggered validation manually.");

        wellnessPage.forceClickScheduleButton();
        ExtentCucumberAdapter.addTestStepLog("Forced click on Schedule button.");
    }

    @Then("I should see validation error messages")
    public void i_should_see_validation_error_messages() {
        wellnessPage.captureErrorMessages();
        ExtentCucumberAdapter.addTestStepLog("Captured error messages.");

        wellnessPage.logInvalidFields();
        ExtentCucumberAdapter.addTestStepLog("Logged invalid fields.");
    }
}
