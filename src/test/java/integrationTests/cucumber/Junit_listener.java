package integrationTests.cucumber;


import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import integrationTests.selenium.Selenium_core;

public class Junit_listener extends RunListener {

	@Override
	public void testRunStarted(Description description) throws Exception {

		//===========================
		//Get system properties set within Maven using the maven-failsafe-plugin
		//Create webdriver to launch browser
		//===========================

		Selenium_core.os = System.getProperty("os");
		Selenium_core.env = System.getProperty("env");	
		Selenium_core.browser = System.getProperty("browser");
		Selenium_core.browserHeadless = Boolean.parseBoolean(System.getProperty("browser.headless"));
		Selenium_core.browserParallelCount = System.getProperty("browser.parallel.count");

		System.out.println("BUILD CONFIGURATION");
		System.out.println("===========================");
		System.out.println("Operating system: " + Selenium_core.os );		
		System.out.println("Web Browser: " + Selenium_core.browser );	
		System.out.println("Browser headless mode: " + Selenium_core.browserHeadless );	
		System.out.println("Browser max parallel count: " + Selenium_core.browserParallelCount );		
		System.out.println("===========================");

		Selenium_core.createWebDriver();
		
	}

	@Override
	public void testRunFinished(Result result) throws Exception {

		String testReport = System.getProperty("user.dir") + "\\target\\Masterthought\\feature-overview.html";	

		System.out.println("---------------------------------------------");
		System.out.println("[Test Report Location] " + testReport);
		System.out.println("---------------------------------------------");
		
		Cucumber_report_generate.GenerateMasterthoughtReport();
		
		Selenium_core.quitWebDriver();
		

	}

	@Override
	public void testStarted(Description description) throws Exception {

	}

	
	@Override
	public void testFinished(Description description) throws Exception {
	
		
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
	
		
	}
	

}