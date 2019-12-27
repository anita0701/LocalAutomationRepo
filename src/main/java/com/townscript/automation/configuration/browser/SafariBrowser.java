package com.townscript.automation.configuration.browser;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SafariBrowser extends AbstractBrowserDriver {

	@Override
	public Capabilities getBrowserCapabilities() {
		DesiredCapabilities cap = DesiredCapabilities.safari();
		cap.setPlatform(Platform.MAC);
		return cap;
	}

	@Override
	public void setSystemVariables() {
		System.setProperty("webdriver.safari.driver", "/usr/bin/safaridriver");
	}
}
