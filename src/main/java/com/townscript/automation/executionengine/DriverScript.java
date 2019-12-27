package com.townscript.automation.executionengine;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import com.townscript.automation.actions.ActionKeywords;
import com.townscript.automation.configuration.reader.ExcelFileReader;
import com.townscript.automation.configuration.reader.PropertyFileReader;
import com.townscript.automation.customexception.InvalidExcelFormatException;

public class DriverScript {

	public static final String JENKINS_BUILD_PATH = "/var/lib/jenkins/workspace/build-automation/";
	public ActionKeywords actionKeywords;
	private String actionKeyword;
	private String pageObject;
	protected Method[] method;
 
	private String testStepID;
	private String testData;
	private String testStepDescription;
	
	public static Properties objectRepository;
	
	public static final String TEST_SERVER = "SERVER";
	public static final String TEST_LOCAL = "LOCAL";
	public static String HOST_NAME;

	public DriverScript() {
		actionKeywords = new ActionKeywords();
		method = actionKeywords.getClass().getMethods();
	}

	public static void main(String[] args) {
		String host = args[0];
		HOST_NAME = host;
		String sheetsPath = PropertyFileReader.getInstance().getSheetsPath() + args[1];
		String objectRepositoryPath = null;
		try {
			switch (HOST_NAME) {
				case TEST_SERVER:
					objectRepositoryPath = JENKINS_BUILD_PATH + PropertyFileReader.getInstance().getObjectRepositoryPath() + "ObjectRepository.properties";
					ExcelFileReader.validateExcelFile(JENKINS_BUILD_PATH + sheetsPath);
					break;
				case TEST_LOCAL:
					objectRepositoryPath = PropertyFileReader.getInstance().getObjectRepositoryPath() + "ObjectRepository.properties";
					ExcelFileReader.validateExcelFile(sheetsPath);
					break;
				default:
					System.out.println("PLEASE ADD IP OF THIS MACHINE!");
			}
		} catch(InvalidExcelFormatException e) {
			e.printStackTrace();
		}
		try (
			FileInputStream objectRepositoryFile = new FileInputStream(objectRepositoryPath);
			){
			objectRepository = new Properties(System.getProperties());
			objectRepository.load(objectRepositoryFile);
			DriverScript startEngine = new DriverScript();
			startEngine.executeTestCase(args[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void executeTestCase(String testSuiteFileName) {
		int testStep;
		int lastStep;
		String testCaseID;
		String executeTest;
		String testScenarioName;
		
		int totalTestCases = ExcelFileReader.getRowCount(testSuiteFileName);
	    System.out.println("Total test Cases: " + totalTestCases);
		
		actionKeywords.startTestReport();
		
		for(int testCase=1; testCase<=totalTestCases; testCase++) {
			testCaseID = ExcelFileReader.getCellData(testCase, PropertyFileReader.getInstance().getTestCaseID(), testSuiteFileName);
			testScenarioName = ExcelFileReader.getCellData(testCase, PropertyFileReader.getInstance().getTestScenarioDescription(), testSuiteFileName);
			executeTest = ExcelFileReader.getCellData(testCase, PropertyFileReader.getInstance().getExecute(), testSuiteFileName);
			System.out.println("Test Case ID: " + testCaseID + "\tExecute: " + executeTest);
			if(executeTest.equalsIgnoreCase("Yes")) {
				actionKeywords.test = actionKeywords.report.createTest(testScenarioName);
				testStep = ExcelFileReader.getRowContains(testCaseID, PropertyFileReader.getInstance().getTestCaseID(), testScenarioName);
				lastStep = ExcelFileReader.getTestStepsCount(testScenarioName, testCaseID, testStep);
				System.out.println("Test Step: " + testStep + "\ttestLastStep: " + lastStep);

				for (;testStep<=lastStep;testStep++) {
					testStepID = ExcelFileReader.getCellData(testStep, PropertyFileReader.getInstance().getTestStepsID(), testScenarioName);
					testStepDescription = ExcelFileReader.getCellData(testStep, PropertyFileReader.getInstance().getTestStepDescription(), testScenarioName);
					actionKeyword = ExcelFileReader.getCellData(testStep, PropertyFileReader.getInstance().getKeyword(), testScenarioName);
					pageObject = ExcelFileReader.getCellData(testStep, PropertyFileReader.getInstance().getPageObject(), testScenarioName);
					testData = ExcelFileReader.getCellData(testStep, PropertyFileReader.getInstance().getTestData(), testScenarioName);
					executeActions();
				    System.out.println("Keyword:" + actionKeyword + "\tPageObject: " + pageObject + "\tTest Data: " + testData + "\tTest Case Step: " + testStepID);

				}
			}
		}	
		actionKeywords.report.flush();
	}

	private void executeActions() { 
		if(method.length > 0) {
			for(int i=0;i<method.length;i++) { 
				try {
					if(method[i].getName().equals(actionKeyword)) {
						method[i].invoke(actionKeywords, pageObject, testStepID, testStepDescription, testData);
						break;
					}
				} catch(IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
		
}
