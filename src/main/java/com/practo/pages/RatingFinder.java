package com.practo.pages;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.practo.utils.WriteToExcel;

public class RatingFinder {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private String mainWindow;
    private boolean isRating;
    private double lastActualRating = Double.NaN;
    private double lastThreshold = Double.NaN;

    private static final Logger logger = LogManager.getLogger(RatingFinder.class);

    public RatingFinder(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.info("RatingFinder initialized.");
    }

    public void open() {
        driver.get("https://www.practo.com/");
        driver.manage().window().maximize();
        mainWindow = driver.getWindowHandle();
    }

    public void searchhospital(String city) {
        try {
            WriteToExcel.log("TestCase0_MaxRatingFinder", "Searching for hospitals in: " + city);
            logger.info("Searching for hospitals in: {}", city);

            WebElement cityInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@placeholder='Search location']")));
            cityInput.clear();
            Thread.sleep(1000);
            cityInput.sendKeys(city);

            wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(@class,'c-omni-suggestion-item')]//div[contains(text(),'" + city + "')]")))
                    .click();

            WebElement search = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@placeholder='Search doctors, clinics, hospitals, etc.']")));
            search.clear();
            search.sendKeys("Hospital");

            Thread.sleep(2000);

            WebElement hospitalOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//div[@id='c-omni-container']//div[contains(@class,'c-omni-suggestion-item__content__title')])[4]")));
            new Actions(driver).moveToElement(hospitalOption).click().perform();

            // Wait for results section to be present
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[@class='line-1']")));

        } catch (Exception e) {
            WriteToExcel.log("TestCase0_MaxRatingFinder", "Error during search: " + e.getMessage());
            logger.error("Error during search", e);
        }
    }

    /**
     * Find rating and assert it meets the threshold.
     * We treat "above" as "at least" (≥) to match typical business expectation.
     */
    public void findrating(String city, double threshold) {
        this.lastThreshold = threshold;

        WriteToExcel.log("TestCase0_MaxRatingFinder",
                String.format("Checking for rating ≥ %.2f: %s", threshold, city));
        logger.info("Checking for rating ≥ {} for hospitals in {}", threshold, city);

        // Wait for the rating cell to be visible
        By ratingCellLocator = By.xpath("//table[@class='hospital-table']//td[text()='Ratings']/following-sibling::td[3]");
        WebElement ratingCell = wait.until(ExpectedConditions.visibilityOfElementLocated(ratingCellLocator));

        String ratingText = ratingCell.getText();           // e.g., "4.5", "4.5 ★", "4.5 out of 5"
        double ratingValue = parseRatingSafely(ratingText); // robust parsing
        // normalize to 1 decimal place (Practo typically shows one decimal)
        ratingValue = Math.round(ratingValue * 10) / 10.0;

        this.lastActualRating = ratingValue;

        double epsilon = 1e-6; // floating point tolerance

        // INCLUSIVE check: ≥ threshold
        boolean ok = ratingValue + epsilon >= threshold;

        // Log before assertion so it shows in report even when failing
        WriteToExcel.log("TestCase0_MaxRatingFinder", String.format("Rating captured: %.2f", ratingValue));
        logger.info("Rating captured: {}", ratingValue);

        Assert.assertTrue(
                String.format("TestCase0_MaxRatingFinder | Expected rating ≥ %.2f, but got %.2f",
                        threshold, ratingValue),
                ok
        );

        isRating = true;
        WriteToExcel.log("TestCase0_MaxRatingFinder", String.format("PASS — Rating ≥ %.2f (actual: %.2f)", threshold, ratingValue));
        logger.info("PASS — Rating ≥ {} (actual: {})", threshold, ratingValue);
    }

    public void result() {
        if (isRating) {
            String msg = String.format("Max Rating meets threshold (≥ %.2f): actual %.2f",
                    lastThreshold, lastActualRating);
            System.out.println(msg);
            WriteToExcel.log("TestCase0_MaxRatingFinder", "Result: " + msg);
            logger.info(msg);
        } else {
            String msg = String.format("Max Rating does NOT meet threshold (≥ %.2f): actual %.2f",
                    lastThreshold, lastActualRating);
            System.out.println(msg);
            WriteToExcel.log("TestCase0_MaxRatingFinder", "Result: " + msg);
            logger.info(msg);
        }
    }

    // ---------- Helpers ----------

    /**
     * Robustly parse rating text like:
     *  "4.5", "4.5 ★", "4.5 out of 5", "4,5", "  4.5  "
     */
    private double parseRatingSafely(String raw) {
        if (raw == null) return Double.NaN;

        // 1) Try to extract the first number with optional decimal using regex
        Pattern p = Pattern.compile("([0-9]+(?:[\\.,][0-9]+)?)");
        Matcher m = p.matcher(raw);
        if (m.find()) {
            String number = m.group(1).replace(',', '.');
            try {
                return Double.parseDouble(number);
            } catch (NumberFormatException ignored) {}
        }

        // 2) Fallback: strip everything except digits/dot/comma and parse
        String cleaned = raw.replaceAll("[^0-9.,]", "").replace(',', '.').trim();
        try {
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            logger.warn("Failed to parse rating from text '{}'", raw, e);
            return Double.NaN;
        }
    }
}