//===================================
/* 
 This is first class to be called by JUnit.
	It's annotated with @RunWith so JUnit framework invokes Cucumber
	as a test runner instead of running the default runner

	We then setup the cucumber run options

*/
//===================================


package integrationTests.cucumber;

import cucumber.api.CucumberOptions;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions( 
		tags={"@Short"},
		//dryRun=true,
		strict = false,
		plugin = {"pretty",
				"html:target/cucumber-html-report", 
				"json:target/cucumber.json",
				"junit:target/cucumber.xml",
				"rerun:target/rerun.txt"}
		,
		features = "src/test/resources/Features/"
		,glue={"integrationTests.selenium.step_definitions","integrationTests.cucumber"}
		)

//maven-failsafe-plugin looks for java files beginning/ending with 'IT' to trigger.
//it finds this java file, which has @RunWith, and so cucumber takes over.
//Cucumber then finds the steps to trigger using both feature files and the step definitions.
//It finds java files containing the step definitions using the glue command above.
public class Cucumber_runConfig_IT {
	//Intentionally blank
		
}

