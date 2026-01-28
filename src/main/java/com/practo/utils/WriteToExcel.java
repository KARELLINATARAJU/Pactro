package com.practo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WriteToExcel {
    private static Workbook workbook;
    private static Map<String, Sheet> sheets = new HashMap<>();
    private static String filePath = "TestOutput/TestExecutionLogs.xlsx";

    static {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                for (Sheet sheet : workbook) {
                    sheets.put(sheet.getSheetName(), sheet);
                }
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void log(String sheetName, String message) {
        // Print to console
        System.out.println(message);

        // Write to Excel
        Sheet sheet;
        if (sheets.containsKey(sheetName)) {
            sheet = sheets.get(sheetName);
        } else {
            sheet = workbook.createSheet(sheetName);
            sheets.put(sheetName, sheet);
        }


        int lastRow = sheet.getLastRowNum();
        Row row = sheet.createRow(lastRow + 1);
        Cell cell = row.createCell(0);
        cell.setCellValue(message);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
