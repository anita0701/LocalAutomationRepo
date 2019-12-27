package com.townscript.automation.configuration.browser;

import static com.townscript.automation.executionengine.DriverScript.JENKINS_BUILD_PATH;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.townscript.automation.configuration.reader.PropertyFileReader;
import com.townscript.automation.executionengine.DriverScript;

public class FirefoxBrowser extends AbstractBrowserDriver {
	
static PropertyFileReader prop = PropertyFileReader.getInstance();

	@Override
	public Capabilities getBrowserCapabilities() {
		DesiredCapabilities cap = DesiredCapabilities.firefox();
		cap.setCapability("marionette", true);
		cap.setBrowserName("firefox");
		cap.setPlatform(Platform.LINUX);
		return cap;
	}

	@Override
	public void setSystemVariables() {
		switch(DriverScript.HOST_NAME) {
		case DriverScript.TEST_SERVER:
			System.setProperty("webdriver.gecko.driver", JENKINS_BUILD_PATH + PropertyFileReader.getInstance().getDriversPath() + "geckodriver");
			break;
		case DriverScript.TEST_LOCAL:
			System.setProperty("webdriver.gecko.driver", PropertyFileReader.getInstance().getDriversPath() + "/geckodriver");
			break;
		default:
			System.out.println("Cannot set system variables. PLEASE ADD IP OF THIS MACHINE");	
		}
				
	}
	
}