package integrationTests.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.testng.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;
import org.testng.annotations.*;
import integrationTests.selenium.main.*;

@CucumberOptions( 
		//Tags (Send using Maven:[clean verify -P qa_build -Dcucumber.options="-t @Retest"]
		features = "src/test/resources/Features/",
		glue={"integrationTests.selenium.step_definitions","integrationTests.cucumber"}
		//Plugin (Json plugin dynamically created per test env. Used for reporting)
		)
public class Runner {

	//==============================================
	// Calls the Grid (or non Grid) webdriver and pass on paramters to it
	//==============================================

	private volatile static int testID;

	private TestNGCucumberRunner testNGCucumberRunner;

	@BeforeClass(alwaysRun = true)
	@Parameters({"operating_system","browser","browser_version","browser_headless"})
	public void setUpClass(
			String operating_system,
			String browser,
			@Optional("") String browser_version,
			@Optional("false") String browser_headless ) throws Exception{

		//System properties set from Maven POM.xml
		Boolean selenium_grid_enabled= Boolean.parseBoolean(System.getProperty("selenium.grid.enabled"));
		String selenium_grid_hub = System.getProperty("selenium.grid.hub");
		Boolean web_proxy= Boolean.parseBoolean(System.getProperty("browsermob.proxy.enabled"));

		if (selenium_grid_enabled){
			//Prerequisite (Ensure Hub and nodes are configured and running)
			WebDriver_factory.setWebDriver(
					operating_system, 
					browser, 
					browser_version, 
					Boolean.parseBoolean(browser_headless), 
					web_proxy, 
					selenium_grid_hub);
		}else{

			WebDriver_factory.setWebDriver(
					operating_system, 
					browser, 
					browser_version, 
					Boolean.parseBoolean(browser_headless), 
					web_proxy, 
					null);
		}


		//==========================
		// Output build configurations being tested
		//==========================	

		synchronized(this){testID++;}

		//Output once
		if (testID == 1){
			System.out.println("Test URL: " + getBaseURL());	
			System.out.println("Selenium Grid Enabled: " + selenium_grid_enabled );	
			if (selenium_grid_enabled) System.out.println("Selenium Grid hub: " + selenium_grid_hub );		
		}

		System.out.println("");
		System.out.println("Starting Test ID: " + testID +
				" (" + operating_system + " " +  browser + ")");

		
		create_unique_json_file(this.getClass(), "plugin", new String [] {"json:target/" + operating_system + "_" + browser + ".json"});
		
	}

	//==========================
	// Using TestNG DataProvider execute cucumber scenarios
	//==========================	

	@Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
	public void scenario(PickleEventWrapper pickleEvent, CucumberFeatureWrapper cucumberFeature) throws Throwable {

		if(WebDriver_factory.getLocalThreadBrowserMobProxyServer() != null){

			WebDriver_factory.getLocalThreadBrowserMobProxyServer().newHar(WebDriver_factory.getLocalThreadOS() + "_" + WebDriver_factory.getLocalThreadBrowser() + ".har");

		}
		
		testNGCucumberRunner.runScenario(pickleEvent.getPickleEvent());
	
	}

	@DataProvider
	public Object[][] scenarios() {

		return testNGCucumberRunner.provideScenarios();


	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() throws Exception {

		testNGCucumberRunner.finish();

		if(WebDriver_factory.getLocalThreadBrowserMobProxyServer() != null){

			WebDriver_factory.getLocalThreadBrowserMobProxyServer().stop();

		}

		//==========================
		// Generate report and quit local thread web driver 
		//==========================	

		if (WebDriver_factory.getLocalThreadWebDriver() != null){

			Report_generator.GenerateMasterthoughtReport();	

			try {
				WebDriver_factory.quitLocalWebDriver();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else{

			System.out.println("There was an issue generating WebDriver for [OS/Browser]:"
					+ " " + WebDriver_factory.getLocalThreadOS() 
					+ "/" + WebDriver_factory.getLocalThreadBrowser() );

		}
	}

	//==========================
	// Cucumber hook used to capture analysis data on failure
	//==========================	

	@After
	public void capture_logs_and_screenshot_on_failure(Scenario scenario) throws Exception{

		if(scenario.isFailed()) {

			Common_methods_and_pom.takeSnapShotAndLogs(scenario.getName());

		}

	}

	//==============================================
	// Use reflection to dynamically change cucumber options (create unique .json files/results).
	//==============================================  

	static volatile boolean firstThread = true;

	private synchronized void create_unique_json_file(Class<?> clazz, String key, Object newValue) throws Exception{  

		//Slightly offset each parralel thread so each gets unique CucumberOptions (.json file name)
		if (!firstThread) Thread.sleep(3000);
		firstThread = false;

		Annotation options = clazz.getAnnotation(CucumberOptions.class);                   //get the CucumberOptions annotation  
		InvocationHandler proxyHandler = Proxy.getInvocationHandler(options);              //setup handler so we can update Annotation using reflection. Basically creates a proxy for the Cucumber Options class
		Field f = proxyHandler.getClass().getDeclaredField("memberValues");                //the annotaton key/values are stored in the memberValues field
		f.setAccessible(true);                                                             //suppress any access issues when looking at f
		@SuppressWarnings("unchecked")
		Map<String, Object> memberValues = (Map<String, Object>) f.get(proxyHandler);      //get the key-value map for the proxy
		memberValues.remove(key);                                                          //renove the key entry...don't worry, we'll add it back
		memberValues.put(key,newValue);     
		//add the new key-value pair. The annotation is now updated.

		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());

	}  

	public static String getBaseURL(){

		return System.getProperty("env.qa.url");

	}

}