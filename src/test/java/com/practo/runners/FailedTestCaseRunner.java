package com.practo.runners;
 
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
 
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "@target/failed_scenarios.txt",
    glue = {"com.practo.stepdefinitions", "com.practo.utils"},
    plugin = {
        "pretty",
        "html:target/rerun-reports/cucumber.html",
        "json:target/rerun-reports/cucumber.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true
)
public class FailedTestCaseRunner {
}