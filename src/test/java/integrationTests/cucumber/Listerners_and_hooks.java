package integrationTests.cucumber;

import java.net.MalformedURLException;

import org.testng.ITestContext ;		
import org.testng.ITestListener ;		
import org.testng.ITestResult ;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import integrationTests.selenium.Selenium_core;		

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
	// TestNG listener triggered after tests completed 
	//==========================	
	
    @Override		
    public void onFinish(ITestContext arg0) {					

    	String testReport = System.getProperty("user.dir") + "\\target\\Masterthought\\feature-overview.html";	

		System.out.println("---------------------------------------------");
		System.out.println("[Test Report Location] " + testReport);
		System.out.println("---------------------------------------------");

		Cucumber_report_generate.GenerateMasterthoughtReport();

		try {
			Selenium_core.quitWebDriver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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