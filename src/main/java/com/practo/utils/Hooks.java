package com.practo.utils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AssumptionViolatedException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

@SuppressWarnings("unused")
public class Hooks {
    public static WebDriver driver;
    private static final Logger logger = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp() {
        String browser = System.getProperty("browser", "chrome"); // default to chrome
        driver = DriverSetup.initializeDriver(browser);
        driver.manage().window().maximize();

        logger.info("==============================");
        logger.info("Running tests on: " + browser);
        logger.info("==============================");
    }

    @After
    public void tearDown(Scenario scenario) {
        logger.info("Scenario completed: " + scenario.getName());
        logger.info("Status: " + scenario.getStatus());

        // Capture screenshot on failure
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");

            // Log to Extent Report
            ExtentCucumberAdapter.addTestStepLog("Scenario failed: " + scenario.getName());
        }
        if (driver != null) {
            driver.quit();
            logger.info("Driver closed.");
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
