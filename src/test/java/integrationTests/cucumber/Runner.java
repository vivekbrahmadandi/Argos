package integrationTests.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import integrationTests.selenium.main.Common_methods_and_pom;
import integrationTests.selenium.main.WebDriver_factory;

@CucumberOptions( 
//		plugin = {"json:target/default.json"},
		features = "src/test/resources/Features/",
		glue={"integrationTests.selenium.step_definitions","integrationTests.cucumber"}
		)
public class Runner {
	
	//==============================================
	// Calls the Grid (or non Grid) webdriver and pass on paramters to it
	//==============================================
	
	private static boolean selenium_grid_enabled; 	
	private static String selenium_grid_hub; 

    private TestNGCucumberRunner testNGCucumberRunner;

    @BeforeClass(alwaysRun = true)
	@Parameters({"operating_system","browser","browser_version","browser_headless"})
    public void setUpClass(
			String operating_system,
			String browser,
			@Optional("") String browser_version,
			@Optional("false") String browser_headless ) throws Exception{
    	
    	
		changeCucumberAnnotation(this.getClass(), "plugin", new String [] {"json:target/" + operating_system + "-" + browser + "-" + Thread.currentThread().getId() + ".json"});
     
		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        
		//Selenium Grid flag/url set in Maven using System properties
		selenium_grid_enabled= Boolean.parseBoolean(System.getProperty("selenium.grid.enabled"));
		selenium_grid_hub = System.getProperty("selenium.grid.hub");

		//Prerequisite (Ensure Hub and nodes are configured and running)
		if (selenium_grid_enabled){
			//Run on Selenium Grid
			new WebDriver_factory().createLocalThreadGridWebDriver(selenium_grid_hub,operating_system,browser,browser_version,Boolean.parseBoolean(browser_headless));

		}else{
			//Run on this build machine
			new WebDriver_factory().createLocalThreadWebDriver(operating_system,browser,Boolean.parseBoolean(browser_headless));

		}


	
		//==========================
		// Output build configuration (Good for showing uniqueness between threads/env tests
		//==========================	
		
		System.out.println("===========================");
		System.out.println("BUILD CONFIGURATION");
		System.out.println("Test URL: " + Common_methods_and_pom.baseURL);	
		System.out.println("Selenium Grid Enabled: " + selenium_grid_enabled );	
		if (selenium_grid_enabled) System.out.println("Selenium Grid hub: " + selenium_grid_hub );	
		System.out.println("Operating system: " + operating_system );
		System.out.println("Web Browser: " + browser );
		System.out.println("Browser headless mode: " + browser_headless );	
		System.out.println("===========================");	
        
    }

    
	//==========================
	// Using TestNG DataProvider execute cucumber scenarios
	//==========================	
    
    @Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void scenario(PickleEventWrapper pickleEvent, CucumberFeatureWrapper cucumberFeature) throws Throwable {
        testNGCucumberRunner.runScenario(pickleEvent.getPickleEvent());
    }

    @DataProvider
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() throws Exception {
        
    	testNGCucumberRunner.finish();
      
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
		}
        
    }
    
    
	//==========================
	// Cucumber hook - Take screenshot on failure
	//==========================	

	@After
	public void takeScreenshotOnFailure(Scenario scenario) throws Exception{

		if(scenario.isFailed()) {

			String dir = System.getProperty("user.dir")  + "\\target\\screenshots_on_failure\\";
			Common_methods_and_pom.takeSnapShot(dir);

		}

	}
    
    
    
	//==============================================
	// Using reflection to dynamically change cucumber options.
	//==============================================  
    
    static int sleep = 0;	
    
	private static void changeCucumberAnnotation(Class<?> clazz, String key, Object newValue) throws Exception{  
		
		//Add a 3 second delay between parallel threads, so each thread gets assigned unique CucumberOptions
    	ThreadLocal<Integer> sleep_threadLocal = new ThreadLocal<Integer>();
    	sleep_threadLocal.set(sleep++);
    	Thread.sleep(3000 * sleep_threadLocal.get());
	
		Annotation options = clazz.getAnnotation(CucumberOptions.class);                   //get the CucumberOptions annotation  
		InvocationHandler proxyHandler = Proxy.getInvocationHandler(options);              //setup handler so we can update Annotation using reflection. Basically creates a proxy for the Cucumber Options class
		Field f = proxyHandler.getClass().getDeclaredField("memberValues");                //the annotaton key/values are stored in the memberValues field
		f.setAccessible(true);                                                             //suppress any access issues when looking at f
		@SuppressWarnings("unchecked")
		Map<String, Object> memberValues = (Map<String, Object>) f.get(proxyHandler);      //get the key-value map for the proxy
		memberValues.remove(key);                                                          //renove the key entry...don't worry, we'll add it back
		memberValues.put(key,newValue);                                                    //add the new key-value pair. The annotation is now updated.
	
	}
    
}