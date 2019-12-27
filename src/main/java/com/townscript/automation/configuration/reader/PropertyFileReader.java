package com.townscript.automation.configuration.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.townscript.automation.executionengine.DriverScript;

import static com.townscript.automation.executionengine.DriverScript.JENKINS_BUILD_PATH;

public class PropertyFileReader { 
	
	private Properties prop = null;
	private static PropertyFileReader propertyFileReader = null;
	private static String testEnvironmentFilePath = "src/main/resources/com/townscript/automation/environment/test-environment-config.properties";

	private PropertyFileReader() {
		super();
		if(prop != null) {
			return;
		}
		prop = new Properties();
		String fileName = null;
		try {
			switch(DriverScript.HOST_NAME) {
				case DriverScript.TEST_SERVER:
					fileName = JENKINS_BUILD_PATH + testEnvironmentFilePath;
					break;
				case DriverScript.TEST_LOCAL:
					fileName = testEnvironmentFilePath;
					break;
				default:
					System.out.println("PLEASE ADD IP OF THIS MACHINE!");
			}
			FileInputStream fis = new FileInputStream(fileName);
			prop.load(fis);
		} catch (IOException e) {
			System.out.println("test-environment-config.properties - FILE NOT FOUND");
			e.printStackTrace();
		}
	}
	
	public static PropertyFileReader getInstance() {
		if(propertyFileReader == null) {
			propertyFileReader = new PropertyFileReader();
		}
		return propertyFileReader;
	}
	
	public String getTownscriptURL() {
		return prop.getProperty("TOWNSCRIPT_URL");
	}
	
	public String getTSDugoutURL() {
		return prop.getProperty("TSDUGOUT_URL");
	}
	
	public int getTestCaseName() {
		return Integer.parseInt(prop.getProperty("Col_TestCaseName"));
	}
	
	public int getTestCaseID() {
		return Integer.parseInt(prop.getProperty("Col_TestCaseID"));
	}
	
	public int getTestStepsID() {
		return Integer.parseInt(prop.getProperty("Col_TestStepsID"));
	}
	
	public int getTestStepDescription() {
		return Integer.parseInt(prop.getProperty("Col_TestStepsDescription"));
	}
	
	public int getKeyword() {
		return Integer.parseInt(prop.getProperty("Col_Keyword"));
	}
	
	public int getPageObject() {
		return Integer.parseInt(prop.getProperty("Col_PageObject"));
	}
	
	public int getTestData() {
		return Integer.parseInt(prop.getProperty("Col_TestData"));
	}
	
	public int getExecute() {
		return Integer.parseInt(prop.getProperty("Col_Execute"));
	}
	
	public int getTestScenarioDescription() {
		return Integer.parseInt(prop.getProperty("Col_TestScenarioDescription"));
	}
   
	public String getDriversPath() {
		return prop.getProperty("DriversPath");
	}
	
	public int getPageLoadTimeout() {
		return Integer.parseInt(prop.getProperty("PageLoadTimeOut"));
	}
	
	public int getImplicitWaitTime() {
		return Integer.parseInt(prop.getProperty("ImplicitWaitTime"));
	}
	
	public int getExplicitWaitTime() {
		return Integer.parseInt(prop.getProperty("ExplicitWaitTime"));
	}
	
	public String getNodeUrl() {
		return prop.getProperty("NodeUrl");
	}
	
	public String getSheetsPath() {
		return prop.getProperty("SheetsPath");
	}
	
	public String getObjectRepositoryPath() {
		return prop.getProperty("ObjectRepositoryPath");
	}
	
	public String getScreenshotsPath() {
		return prop.getProperty("ScreenshotsPath");
	}
	
	public String getTestReportsPath() {
		return prop.getProperty("TestReportPath");
	}
	
	public String getBaseUrl() {
		return prop.getProperty("BASE_URL");
	}
}