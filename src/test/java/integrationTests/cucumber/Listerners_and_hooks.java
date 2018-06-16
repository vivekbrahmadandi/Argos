package integrationTests.cucumber;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.ITestContext ;		
import org.testng.ITestListener ;		
import org.testng.ITestResult ;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import integrationTests.selenium.main.Selenium_core;
import integrationTests.selenium.main.WebDriver_control;		

public class Listerners_and_hooks implements ITestListener						
{	

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

		Cucumber_report_generate.GenerateMasterthoughtReport();

		String testReportLocation = null;
		
		try {
			testReportLocation = moveReports();
		} catch (IOException e) {

			e.printStackTrace();
		}


		System.out.println("---------------------------------------------");
		System.out.println("[Test Report Location] " + testReportLocation);
		System.out.println("---------------------------------------------");

		try {
			WebDriver_control.quitWebDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}	

	//Enable unique Masterthought reporting for each environment setup. 
	public String moveReports() throws IOException{

		File newDir = null;
		
		File dir = new File(System.getProperty("user.dir") + "\\target\\Masterthought");
		if (!dir.isDirectory()) {
			System.err.println("There is no directory @ given path");
		} else {

			newDir = new File(dir.getParent() + "\\" + "Masterthought-"  + Cucumber_runner.operating_system + "-" + Cucumber_runner.browser);
			dir.renameTo(newDir);
		}

		return newDir.getPath();
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