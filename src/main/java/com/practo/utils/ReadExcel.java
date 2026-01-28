package com.practo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcel {

    public static String[] readFormData(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet("Sheet1");
        Row row = sheet.getRow(1);

        String[] data = new String[6];
        for (int i = 0; i < 6; i++) {
            Cell cell = row.getCell(i);
            data[i] = cell != null ? cell.toString() : "";
        }

        workbook.close();
        fis.close();
        return data;
    }
}
