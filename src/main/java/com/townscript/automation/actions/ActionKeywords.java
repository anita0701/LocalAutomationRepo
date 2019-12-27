package com.townscript.automation.actions;

import static com.townscript.automation.executionengine.DriverScript.objectRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.townscript.automation.configuration.browser.InitWebDriver;
import com.townscript.automation.configuration.reader.PropertyFileReader;
import com.townscript.automation.customexception.DriverNotFoundException;
import com.townscript.automation.customexception.InvalidLocatorException;
import com.townscript.automation.utils.Utility;

public class ActionKeywords {

	private WebDriver driver;
	private WebDriverWait wait;
	
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports report;
	public ExtentTest test;
	
	InitWebDriver webDriver = new InitWebDriver();
	
	public ActionKeywords() {
		htmlReporter = new ExtentHtmlReporter(PropertyFileReader.getInstance().getTestReportsPath() + "TestReport.html");
		report = new ExtentReports();
	}
	
	public void openBrowser(String object, String stepID, String description, String browser) {
		try {
			webDriver.setUpBrowser(browser);
			driver = webDriver.getDefaultDriver();
			wait = new WebDriverWait(driver, PropertyFileReader.getInstance().getExplicitWaitTime());
			test.log(Status.PASS, description + " - " + browser);
		} catch(DriverNotFoundException e) {
			test.log(Status.FAIL, stepID + " - " + description + e.getMessage());
		}
	}
	
	public void openWebsite(String object, String stepID, String description, String website) {
		try {
			if(Utility.linkExists(website)) {  
				driver.get(website);
				test.log(Status.PASS, description + " - " + website);
			} else {
				test.log(Status.FAIL, stepID + " - " + description + "--DISCONNECTED FROM THE INTERNET / INTERNET IS SLOW--");
			}
		} catch(Exception e) {
			test.log(Status.FAIL, stepID + " - " + description + "" + e.getMessage());
		}
	}
	
	public void select(String object, String stepID, String description, String data) throws IOException {
		try {
			Select sel = new Select(driver.findElement(
					getWebElement(objectRepository.getProperty(object), data)));
			sel.selectByVisibleText(data);			
			test.log(Status.PASS, description + " - " + data);
		}
			catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void click(String object, String stepID, String description, String data) throws IOException, InvalidLocatorException {
		try {
			WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(
					getWebElement(objectRepository.getProperty(object), data)));
			if(object.equals("homepage.searchresult.city1")) {
				Thread.sleep(2000);
			} else if(object.equals("back_button")) {
				Thread.sleep(4000);
			}
			Thread.sleep(2000);
			webElement.click();
			test.log(Status.PASS, description);
		}
			catch(org.openqa.selenium.StaleElementReferenceException |  org.openqa.selenium.NoSuchElementException ex)
			{
				WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(
						getWebElement(objectRepository.getProperty(object), data)));
				webElement.click();
				test.log(Status.PASS, description);
			}
		 catch (InvalidLocatorException | NoSuchElementException e) {
				test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
						MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		} catch (InterruptedException e) {
				// restore interrupted state
				Thread.currentThread().interrupt(); 
		}
	}

	public void type(String object, String stepID, String description, String data) throws IOException {
		try {
			WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(
					getWebElement(objectRepository.getProperty(object), data)));
			if(objectRepository.getProperty(object).contains("iframe")) {
				driver.switchTo().frame(webElement);
				webElement = driver.findElement(By.tagName("body"));
			}
			webElement.click();
			webElement.clear();
			webElement.sendKeys(data);
			driver.switchTo().defaultContent();
			test.log(Status.PASS, description + " - " + data);
		} catch(InvalidLocatorException | NoSuchElementException | TimeoutException e) {
			test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
	
	public void verifyText(String object, String stepID, String description, String expectedText) throws IOException {
		try {
			WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
					getWebElement(objectRepository.getProperty(object), expectedText)));
			
	       // driver.manage().timeouts().pageLoadTimeout(PropertyFileReader.getInstance().getPageLoadTimeout(), TimeUnit.SECONDS);
            System.out.println(webElement.getText());
			if(webElement.getText().contains(expectedText)) {
				test.log(Status.PASS, description + " - " + expectedText);
				//System.out.println("Original text present in Website: " +webElement.getText());
			} else {
				test.log(Status.FAIL, stepID + " - " + description +"\n" ,
						MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
			}
		} catch (InvalidLocatorException | NoSuchElementException | TimeoutException e) {
			test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
	
	public void verifyPageTitle(String object, String stepID, String description, String expectedTitle) throws IOException {
		if(wait.until(ExpectedConditions.titleContains(expectedTitle))) {
			test.log(Status.PASS, description + " - " + expectedTitle);
		} else {
			test.log(Status.FAIL, stepID + " - " + description,
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
	
	public void verifyElementPresent(String object, String stepID, String description, String data) throws IOException {
		try {
			wait.until(ExpectedConditions.visibilityOf(
					(WebElement) getWebElement(objectRepository.getProperty(object), data)));
			test.log(Status.PASS, description + " - " + data);
		} catch (InvalidLocatorException | NoSuchElementException | TimeoutException e) {
			test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
	
	public void verifyList(String object, String stepID, String description, String expectedList) throws IOException {
		boolean isReported = false;
		try {
			List<String> expectedDataList = Arrays.asList(expectedList.split(","));
			List<WebElement> webElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
					getWebElement(objectRepository.getProperty(object), expectedList)));
			if(Utility.equalUnorderedLists(expectedDataList, webElements)) {
				if(!isReported) {
					test.log(Status.PASS, description + " - " + expectedList);
					isReported = true;
				} else {
					test.log(Status.FAIL, stepID + " - " + description,
							MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
					isReported = true;
				}
			} else {
				test.log(Status.FAIL, stepID + " - " + description,
						MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
			}
		} catch(InvalidLocatorException | NoSuchElementException | TimeoutException e) {
			test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
	
	public void quitBrowser(String object, String stepID, String description, String data) throws IOException {
		try {
			webDriver.tearDown();
			test.log(Status.PASS, description);
		} catch (Exception e) {
			test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}

	public void navigateBack(String object, String stepID, String description, String data) throws IOException {
		try {
			driver.navigate().back();
			test.log(Status.PASS, description);
		} catch (Exception e) {
			test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
	
	public void navigateToUrl(String object, String stepID, String description, String data) throws IOException {
		try {
			String url = PropertyFileReader.getInstance().getTSDugoutURL() + data; 

			driver.navigate().to(url);
			test.log(Status.PASS, description);
		} catch (Exception e) {
			test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
		
	public static By getWebElement(String pageObject, String data) throws InvalidLocatorException 
	{
		if(pageObject.startsWith("id")) {
			pageObject = pageObject.replace("id=", "");
			return By.id(pageObject);
		} else if(pageObject.startsWith("name")) {
			pageObject = pageObject.replace("name=", "");
			return By.name(pageObject);
		} else if(pageObject.startsWith("xpath")) {
			if(pageObject.contains("*var*")) {
				pageObject = pageObject.replace("*var*", data);
			}
			pageObject = pageObject.replace("xpath=", "");
			return By.xpath(pageObject);
		} else if(pageObject.startsWith("className")) {
			pageObject = pageObject.replace("className=", "");
			return By.className(pageObject);
		} else if(pageObject.startsWith("css")) {
			pageObject = pageObject.replace("css=", "");
			return By.cssSelector(pageObject);
		} else if(pageObject.startsWith("linkText")) {
			pageObject = pageObject.replace("linkText=", "");
			return By.linkText(pageObject);
		} else if(pageObject.startsWith("partialLinkText")) {
			pageObject = pageObject.replace("partialLinkText=", "");
			return By.partialLinkText(pageObject);
		} else if(pageObject.startsWith("tagName")) {
			pageObject = pageObject.replace("tagName=", "");
			return By.tagName(pageObject);
		} else {
			throw new InvalidLocatorException(pageObject);
		}
	}
	
	public void verifyCurrentUrl(String object, String stepID, String description, String data) throws IOException, InterruptedException {
		
		String expectedUrl = PropertyFileReader.getInstance().getTSDugoutURL() + data; 
		if(wait.until(ExpectedConditions.urlMatches(expectedUrl))){
			System.out.println(driver.getCurrentUrl());
			test.log(Status.PASS, description + " - " + expectedUrl);
		} else {
			test.log(Status.FAIL, stepID + " - " + description,
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
	
	public void hoverAndClick(String object, String stepID, String description, String data) throws IOException
	{
		try {
			Thread.sleep(3000);
			WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable( 
					getWebElement(objectRepository.getProperty(object), data)));
			Actions action = new Actions(driver);
			action.moveToElement(webElement).click(webElement).build().perform();
			test.log(Status.PASS, description);
			
		} catch (InvalidLocatorException | NoSuchElementException e) {
				test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
						MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
		catch (InterruptedException e) {
				// restore interrupted state
				Thread.currentThread().interrupt(); 
		}
		catch(Exception ex)
		{
			System.out.println("Cause is :" +ex.getCause());
		}
	
	}
	
	public void getElementColor(String object, String stepID, String description, String data) throws IOException {
		try {
			WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
					getWebElement(objectRepository.getProperty(object), data)));
		
			String symbolColor = webElement.getCssValue("background-color");
			String color = webElement.getCssValue("color");
			
			String hex = Color.fromString(color).asHex();
			System.out.println("hex value: "+hex);
			
			
			System.out.println("Element background Color: "+symbolColor);
			System.out.println("Element Color: "+color);
			test.log(Status.PASS, description);
		} catch (InvalidLocatorException | NoSuchElementException e) {
				test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
						MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
	
	public void clearTextBox(String object, String stepID, String description, String data) throws IOException, InterruptedException {
		try {
			WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
					getWebElement(objectRepository.getProperty(object), data)));
			webElement.clear();
			test.log(Status.PASS, description);
		} catch (InvalidLocatorException | NoSuchElementException e) {
				test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
						MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}
	
	public void getButtonStatus(String object, String stepID, String description, String data) throws Exception
	{
		WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
				getWebElement(objectRepository.getProperty(object), data)));
		
	
		System.out.println("is enabled status: "+webElement.isEnabled());
		
	//	Boolean disabled = driver.executeScript("return arguments[0].hasAttribute(\"disabled\");", webElement);

		if(webElement.isDisplayed() && webElement.isEnabled())
		{
			webElement.click();
			System.out.println("Button is enabled");
			
		} else
		{
			System.out.println("Button is disabled");
			
		}
	}
	
	public void searchLocation(String object, String stepID, String description, String data) throws Exception
	{
		Thread.sleep(3000);
		String dropdownLocator = "header.locationchange.dropdown";
		String locations[]=data.split(",");
		for(String loc:locations)
		{
 
		click(dropdownLocator,  stepID, description, loc);
		
		String textboxSearchLocator = "header.searchlocation.textbox";
		type(textboxSearchLocator,  stepID, description, loc);
		
		String location1locator = "header.searchlocation.result1";
		click(location1locator,  stepID, description, loc);

		
		}			
	
	}
	
	public void startTestReport() {
		report.attachReporter(htmlReporter);
		report.setSystemInfo("Host Name", "Townscript");
		report.setSystemInfo("Environment", "Staging Environment");
		report.setSystemInfo("User Name", "Anita");
		htmlReporter.config().setDocumentTitle("Townscript Test Automation Report");
		htmlReporter.config().setReportName("Regression Test Report");
		htmlReporter.config().setTheme(Theme.STANDARD);
	}
	public void clickListElement(String object, String stepID, String description, String data)
			throws IOException, InvalidLocatorException, InterruptedException {
		try {
			List<WebElement> webElement = null;
			webElement.get(0).click();
			test.log(Status.PASS, description);
		} catch (NoSuchElementException e) {
			test.log(Status.FAIL, stepID + " - " + description + "\n" + e.getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(Utility.captureScreenshot(driver)).build());
		}
	}

	
	

}