package com.townscript.automation.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.townscript.automation.configuration.reader.PropertyFileReader;

public class Utility {

	private Utility() {

	}

	public static String captureScreenshot(WebDriver driver) {
		PropertyFileReader prop = PropertyFileReader.getInstance();
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File source = screenshot.getScreenshotAs(OutputType.FILE);
		String imagePath = prop.getScreenshotsPath() + System.currentTimeMillis() + ".png";
		File destination = new File(prop.getTestReportsPath() + imagePath);
		try {
			FileUtils.copyFile(source, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imagePath;
	}

	public static boolean equalUnorderedLists(List<String> listOne, List<WebElement> listTwo) {
		List<String> expectedList = new ArrayList<>();
		List<String> webElementToStringList = new ArrayList<>();
		if (listOne == null && listTwo == null) {
			return true;
		}

		listOne = new ArrayList<>(listOne);
		listTwo = new ArrayList<>(listTwo);

		if (listOne.size() == listTwo.size()) {
			for (int i = 0; i < listOne.size(); i++) {
				expectedList.add(listOne.get(i).trim());
				webElementToStringList.add(listTwo.get(i).getText().trim());
			}
		} else {
			return false;
		}

		Collections.sort(listOne);
		Collections.sort(webElementToStringList);

		return listOne.equals(webElementToStringList);
	}
	
	public static boolean linkExists(String URL){
	    try {
	        HttpURLConnection.setFollowRedirects(false);
	        HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();
	        conn.setRequestMethod("HEAD"); // Using HEAD since we wish to fetch only meta data
	        return (conn.getResponseCode() == HttpURLConnection.HTTP_OK ||
					(conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) ||
					(conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP));
	    } catch (Exception e) {
	    	System.out.println(e);
	        return false;
	    }
	}

}