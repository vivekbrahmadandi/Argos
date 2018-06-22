package integrationTests.cucumber;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext ;		
import org.testng.ITestListener ;		
import org.testng.ITestResult ;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import integrationTests.selenium.main.Selenium_core;
import integrationTests.selenium.main.WebDriver_factory;		

public class Listerners_and_hooks implements ITestListener	{	

	//==========================
	// Cucumber hook triggered after each scenerio. 
	//==========================	

	@After
	public void takeScreenshotOnFailure(Scenario scenario) throws Exception{

		if(scenario.isFailed()) {

			String dir = System.getProperty("user.dir")  + "\\target\\screenshots_on_failure\\";
			Selenium_core.takeSnapShot(dir);

		}

	}

	//==========================
	// TestNG listener triggered after tests complete generates master thought Report
	//==========================	

	@Override		
	public void onFinish(ITestContext arg0) {					

		if (Selenium_core.webdriver.get() != null){


			Cucumber_report_generate.GenerateMasterthoughtReport();
			
			String testReportLocation = null;
			try {
				testReportLocation = Cucumber_report_generate.moveReports();
				
				System.out.println("---------------------------------------------");
				System.out.println("[Test Report Location] " + testReportLocation);
				System.out.println("---------------------------------------------");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				WebDriver_factory.quitWebDriver();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	//Not used
	@Override	
	public void onStart(ITestContext arg0) { }		

	@Override		
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {}		

	@Override		
	public void onTestFailure(ITestResult arg0) {}		

	@Override		
	public void onTestSkipped(ITestResult arg0) { }		

	@Override		
	public void onTestStart(ITestResult arg0) {}		

	@Override		
	public void onTestSuccess(ITestResult arg0) {}	

}



