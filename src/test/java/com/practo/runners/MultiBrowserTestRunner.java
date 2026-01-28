package com.practo.runners;

import com.practo.utils.WriteToExcel;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiBrowserTestRunner {
	
	private static String extractSheetNameFromMessage(String message) {
	    if (message == null) return null;

	    // Look for pattern like "TestCaseX_SomeName | ..."
	    Pattern pattern = Pattern.compile("(TestCase\\d+_[A-Za-z]+)");
	    Matcher matcher = pattern.matcher(message);

	    if (matcher.find()) {
	        return matcher.group(1);
	    }
	    return null;
	}
    public static void main(String[] args) {
        String[] browsers = {"chrome", "edge"};

        // Map test class names to sheet names
        Map<String, String> testClassToSheet = new HashMap<>();
        testClassToSheet.put("TestCase3_InvalidForm", "TestCase3_InvalidForm");
        testClassToSheet.put("TestCase2_TopCities", "TestCase2_TopCities");
        testClassToSheet.put("TestCase1_HospitalFilter", "TestCase1_HospitalFilter");
        testClassToSheet.put("TestCase0_MaxRatingFinder", "TestCase0_MaxRatingFinder");

        for (String browser : browsers) {
            System.setProperty("browser", browser);

            // Log start of test run to all sheets
            for (String sheet : testClassToSheet.values()) {
                WriteToExcel.log(sheet, "==============================");
                WriteToExcel.log(sheet, "Running tests on: " + browser);
                WriteToExcel.log(sheet, "==============================");
            }

            Result result = JUnitCore.runClasses(TestRunner.class);

            // Log failures only to the relevant sheet
            for (Failure failure : result.getFailures()) {
                String message = failure.getMessage();
                String sheetName = extractSheetNameFromMessage(message); // implement this
                if (sheetName != null) {
                    WriteToExcel.log(sheetName, "Failure: " + failure.toString());
                } else {
                    System.out.println("Could not determine sheet for failure: " + failure.toString());
                }
            }
            File failedFile = new File("target/failed_scenarios.txt");
            if (failedFile.exists() && failedFile.length() > 0) {
                System.out.println("Re-running failed scenarios for browser: " + browser);
                JUnitCore.runClasses(FailedTestCaseRunner.class);
            }
            // Log summary to all sheets
            for (String sheet : testClassToSheet.values()) {
                WriteToExcel.log(sheet, "Tests run: " + result.getRunCount());
                WriteToExcel.log(sheet, "Tests failed: " + result.getFailureCount());
                WriteToExcel.log(sheet, "Tests ignored: " + result.getIgnoreCount());
                WriteToExcel.log(sheet, "Successful: " + result.wasSuccessful());
            }
        }
    }
}
