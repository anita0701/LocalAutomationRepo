package com.townscript.automation.configuration.browser;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class AbstractBrowserDriver {
	
	public abstract Capabilities getBrowserCapabilities();
	public abstract void setSystemVariables();
	
	public WebDriver getDriver(String nodeUrl) throws MalformedURLException {
		setSystemVariables();
		WebDriver driver = new RemoteWebDriver(new URL(nodeUrl), getBrowserCapabilities());
		driver.manage().deleteAllCookies();
		return driver;
	}
	
	public Platform platform() {
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("mac")) {
			return Platform.MAC;
		} else if (os.toLowerCase().contains("windows")) {
			return Platform.WINDOWS;
		} else if (os.toLowerCase().contains("linux")) {
			return Platform.LINUX;
		}
		return Platform.ANY;
	}
}
