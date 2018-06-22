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
import integrationTests.selenium.main.Common_methods_and_pom;
import integrationTests.selenium.main.WebDriver_factory;		

public class Listerners_and_hooks implements ITestListener	{	

	//==========================
	// Cucumber hook triggered after each scenerio. 
	//==========================	

	@After
	public void takeScreenshotOnFailure(Scenario scenario) throws Exception{

		if(scenario.isFailed()) {

			String dir = System.getProperty("user.dir")  + "\\target\\screenshots_on_failure\\";
			Common_methods_and_pom.takeSnapShot(dir);

		}

	}

	//==========================
	// TestNG listener triggered after tests complete generates master thought Report
	//==========================	

	@Override		
	public void onFinish(ITestContext arg0) {					

		if (WebDriver_factory.getLocalThreadWebDriver() != null){

			Report_generator.GenerateMasterthoughtReport();
			
			String testReportLocation = null;
			try {
				testReportLocation = Report_generator.moveReports();
				
				System.out.println("---------------------------------------------");
				System.out.println("[Test Report Location] " + testReportLocation);
				System.out.println("---------------------------------------------");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				WebDriver_factory.quitLocalWebDriver();
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



