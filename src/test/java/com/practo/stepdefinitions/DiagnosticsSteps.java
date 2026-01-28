package com.practo.stepdefinitions;

import com.practo.pages.DiagnosticsPage;
import com.practo.utils.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import java.time.Duration;

public class DiagnosticsSteps {
    DiagnosticsPage diagnosticsPage;

    @Given("I navigate to the diagnostics page")
    public void i_navigate_to_diagnostics_page() {
        Hooks.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        Hooks.getDriver().get("https://www.practo.com");
        ExtentCucumberAdapter.addTestStepLog("Opened Practo homepage.");

        diagnosticsPage = new DiagnosticsPage(Hooks.getDriver());
        diagnosticsPage.navigateToLabTests();
        ExtentCucumberAdapter.addTestStepLog("Navigated to Lab Tests section.");
    }

    @Then("I should see a list of top cities")
    public void i_should_see_list_of_top_cities() {
        diagnosticsPage.getTopCities();
        ExtentCucumberAdapter.addTestStepLog("Extracted top cities from diagnostics page.");
    }
}
