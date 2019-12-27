package com.townscript.automation.configuration.browser;

import static com.townscript.automation.executionengine.DriverScript.JENKINS_BUILD_PATH;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.townscript.automation.configuration.reader.PropertyFileReader;
import com.townscript.automation.executionengine.DriverScript;

public class ChromeBrowser extends AbstractBrowserDriver {
	
	@Override
	public Capabilities getBrowserCapabilities() {
		DesiredCapabilities caps = new DesiredCapabilities();
		ChromeOptions chromeOptions = new ChromeOptions();
		switch(DriverScript.HOST_NAME) {
			case DriverScript.TEST_SERVER:
				chromeOptions.addArguments("--kiosk");
				chromeOptions.addArguments("start-maximized"); // open Browser in maximized mode
				chromeOptions.addArguments("--no-sandbox"); // Bypass OS security model
				chromeOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
				chromeOptions.setExperimentalOption("useAutomationExtension", false);
				chromeOptions.addArguments("--headless", "--window-size=1920,1200", "--ignore-certificate-errors");
				chromeOptions.addArguments("--disable-setuid-sandbox");
				chromeOptions.addArguments("--proxy-server='direct://'");
				chromeOptions.addArguments("--proxy-bypass-list=*");
				caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				caps.setPlatform(Platform.LINUX);
				break;
			case DriverScript.TEST_LOCAL:
				chromeOptions.addArguments("--kiosk");
				chromeOptions.addArguments("start-maximized");
				caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				caps.setPlatform(platform());
				break;
			default:
				System.out.println("Cannot get browser capabilities. PLEASE ADD IP OF THIS MACHINE");	
		}
		return caps;
	}

	@Override
	public void setSystemVariables() {
		switch(DriverScript.HOST_NAME) {
			case DriverScript.TEST_SERVER:
				System.setProperty("webdriver.chrome.driver", JENKINS_BUILD_PATH + PropertyFileReader.getInstance().getDriversPath() + "/Windows/chromedriver.exe");
				break;
			case DriverScript.TEST_LOCAL:
				System.setProperty("webdriver.chrome.driver", PropertyFileReader.getInstance().getDriversPath() + "/chromedriver.exe");
				//System.setProperty("webdriver.chrome.driver", "/home/anita/eclipse-workspace/TS_Automation_project/automation-scripts/resources/webdrivers/ubuntu" + "/chromedriver");
				break;
			default:
				System.out.println("Cannot set system variables. PLEASE ADD IP OF THIS MACHINE");	
		}
	}
	
}
