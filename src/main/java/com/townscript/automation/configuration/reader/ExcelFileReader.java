package com.townscript.automation.configuration.reader;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.townscript.automation.customexception.InvalidExcelFormatException;

public class ExcelFileReader {
	
	private ExcelFileReader() {
		
	}

	private static XSSFSheet excelWSheet;
	private static XSSFWorkbook excelWBook;
	private static PropertyFileReader prop = PropertyFileReader.getInstance();
	
	// Validate Excel File
	public static void validateExcelFile(String file) throws InvalidExcelFormatException {	
		String fileExtensionName = file.substring(file.indexOf('.'));
		if(fileExtensionName.equals(".xlsx") || fileExtensionName.equals(".xls")) {
			setExcelFile(file);
		} else {
			throw new InvalidExcelFormatException("Unsupported File Format. Please upload .xlsx or .xls file");
		}
	}

	//Open Excel File
	public static void setExcelFile(String file) {
		try (
			FileInputStream excelFile = new FileInputStream(file);
		) {
			excelWBook = new XSSFWorkbook(excelFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	//Read Data From Excel File
	public static String getCellData(int rowNum, int colNum, String sheetName) {
		XSSFCell cell;
		excelWSheet = excelWBook.getSheet(sheetName);
		try {
			cell = excelWSheet.getRow(rowNum).getCell(colNum);
			return cell.getStringCellValue();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	//Row Count in Excel Sheet
	public static int getRowCount(String sheetName) {
		int number = 0;
		try {
			excelWSheet = excelWBook.getSheet(sheetName);
			number = excelWSheet.getLastRowNum();
		} catch(Exception e) { 
			e.printStackTrace();
		}
		return number;
	}
	
	//Row number of Test Case
	public static int getRowContains(String testCaseName, int colNum, String sheetName){
		int rowNum=0;
		try {
			int rowCount = ExcelFileReader.getRowCount(sheetName);
			for (; rowNum < rowCount; rowNum++) {
				if (ExcelFileReader.getCellData(rowNum, colNum, sheetName).equalsIgnoreCase(testCaseName)) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowNum;
	}
	
	//Count of Test Steps of Test Case
	public static int getTestStepsCount(String sheetName, String sTestCaseID, int iTestCaseStart) {
		try {
			for (int i = iTestCaseStart; i <= ExcelFileReader.getRowCount(sheetName); i++) {
				if (!sTestCaseID.equals(ExcelFileReader.getCellData(i, prop.getTestCaseID(), sheetName))) {
					return i;
				}
			}
			excelWSheet = excelWBook.getSheet(sheetName);
			return excelWSheet.getLastRowNum();
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

} 