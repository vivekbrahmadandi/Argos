package integrationTests.cucumber;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import integrationTests.selenium.main.Selenium_core;
import integrationTests.selenium.main.WebDriver_factory;

@CucumberOptions( 
		tags={"@Basket"},
		//dryRun=true,
		strict = false,
		plugin = {
				"json:target/cucumber.json",
		"rerun:target/rerun.txt"}
		,
		features = "src/test/resources/Features/"
		,glue={"integrationTests.selenium.step_definitions","integrationTests.cucumber"}
		)
public class Cucumber_runner extends AbstractTestNGCucumberTests{

	public static boolean selenium_grid_enabled; 	
	public static String selenium_grid_hub; 

	//Will be referenced when generating the master thought report names 
	public static ThreadLocal<String> operating_system = new ThreadLocal<String>();
	public static ThreadLocal<String> browser = new ThreadLocal<String>();
	
	//==========================
	// TestNG hook triggered before tests.  
	//==========================	

	@Parameters({"operating_system","browser","browser_version","browser_headless"})
	@BeforeClass
	public void get_test_configurations(
			String operating_system,
			String browser, 
			@Optional("")  String browser_version, 
			@Optional("false") String browser_headless ) throws MalformedURLException, InterruptedException{

		
		//Selenium Grid flag/url set in Maven using System properties
		selenium_grid_enabled= Boolean.parseBoolean(System.getProperty("selenium.grid.enabled"));
		selenium_grid_hub = System.getProperty("selenium.grid.hub");
		
		System.out.println("");
		System.out.println("Running test thread: " + Thread.currentThread().getId());
		System.out.println("");
		
		System.out.println("BUILD CONFIGURATION");
		System.out.println("===========================");
		System.out.println("Test URL: " + Selenium_core.baseURL);	
		System.out.println("Selenium Grid Enabled: " + selenium_grid_enabled );	
		if (selenium_grid_enabled) System.out.println("Selenium Grid hub: " + selenium_grid_hub );	

		System.out.println("Operating system: " + operating_system );
		System.out.println("Web Browser: " + browser );
		System.out.println("Browser headless mode: " + browser_headless );		
		System.out.println("===========================");	

		Cucumber_runner.operating_system.set(operating_system);
		Cucumber_runner.browser.set(browser);

		//Prerequisite (Ensure Hub and nodes are configured and running)
		if (selenium_grid_enabled){
			//Run on Selenium Grid
			WebDriver_factory.createGridWebDriver(selenium_grid_hub,operating_system,browser,browser_version,Boolean.parseBoolean(browser_headless));

		}else{
			//Run on this build machine
			WebDriver_factory.createStandardWebDriver(operating_system,browser,Boolean.parseBoolean(browser_headless));

		}

	}

}

