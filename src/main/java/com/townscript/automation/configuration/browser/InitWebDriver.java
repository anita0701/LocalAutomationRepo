package com.townscript.automation.configuration.browser;

import java.net.MalformedURLException;
import org.openqa.selenium.WebDriver;
import com.townscript.automation.configuration.reader.PropertyFileReader;
import com.townscript.automation.customexception.DriverNotFoundException;

public class InitWebDriver {

	protected WebDriver driver;

	public void setUpBrowser(String browser) throws DriverNotFoundException {
		switch (browser.toUpperCase()) {
			case "FIREFOX":
				try {
					driver = new FirefoxBrowser().getDriver(PropertyFileReader.getInstance().getNodeUrl());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;
			case "CHROME":
				try {
					driver = new ChromeBrowser().getDriver(PropertyFileReader.getInstance().getNodeUrl());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;
			case "SAFARI":
				try {
					driver = new SafariBrowser().getDriver(PropertyFileReader.getInstance().getNodeUrl());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;
			default:
				throw new DriverNotFoundException(browser);
		}
	}
	
	public WebDriver getDefaultDriver() {
		return driver;
	}
	
	public void tearDown() {
		if(driver != null) {
			driver.quit();
		}
	}
	
}
