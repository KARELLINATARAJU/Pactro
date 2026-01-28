package com.practo.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    WebDriver driver;
    WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(HomePage.class);

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        logger.info("HomePage initialized.");
    }

    By forCorporatesMenu = By.xpath("//span[text()='For Corporates']");
    By healthWellnessOption= By.xpath("//a[contains(text(),'Health & Wellness Plans')]");

    public void goToCorporateWellness() {
        logger.info("Navigating to 'For Corporates' menu.");
        Actions actions = new Actions(driver);
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(forCorporatesMenu));
        actions.moveToElement(menu).click().perform();

        logger.info("Clicking on 'Health & Wellness Plans' option.");
        WebElement firstOption = wait.until(ExpectedConditions.elementToBeClickable(healthWellnessOption));
        firstOption.click();
    }
}
