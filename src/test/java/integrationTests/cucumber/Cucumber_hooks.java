package integrationTests.cucumber;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import integrationTests.selenium.Selenium_core;

public class Cucumber_hooks {

	@After
    public void afterScenario(Scenario scenario) throws Exception{
		
		if(scenario.isFailed()) {

			String dir = System.getProperty("user.dir")  + "\\target\\screenshots_on_failure\\";
			Selenium_core.takeSnapShot(dir);

		}
		
    }
	
	
}
