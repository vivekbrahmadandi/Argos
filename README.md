[Link to framework](https://github.com/workpeter/ARGOS)   
I created this framework to use as a template for future projects. Often projects have an existing mature framework, but that's fine because there are lots of things this framework does well, which can be pinched to improve existing frameworks. 

# Selenium BDD framework 

This framework enables testing across multiple environment configurations in parallel then consolidates the results into a single Cucumber report. Here is an example report:  
* [Feature Overview](https://ibb.co/d11ezo)
* [Scenario breakdown](https://ibb.co/jWXf5T)
* [Scenario breakdown (with failing step)](https://ibb.co/fHbq5T)

## Author

* **Peter Anderson (peter.x4000@gmail.com)** 

## Built With

* [Java](https://en.wikipedia.org/wiki/Java_(programming_language)) - Programming language
* [Selenium (inc. Selenium Grid)](https://en.wikipedia.org/wiki/Selenium_(software)) - Automate web applications
* [Cucumber-JVM](https://github.com/cucumber/cucumber-jvm) - Java implementation of Cucumber
* [Maven](https://en.wikipedia.org/wiki/Apache_Maven) - Build automation tool
* [TestNG](https://en.wikipedia.org/wiki/TestNG) - Testing framework
* [Browsermob web proxy](https://github.com/lightbody/browsermob-proxy) - Web proxy to capture HTTP content
* [Master Thoughts](https://mvnrepository.com/artifact/net.masterthought/cucumber-reporting) - Provides pretty html reports for Cucumber
* [SOAPUI](https://en.wikipedia.org/wiki/SoapUI) - Web service testing (SOAP and REST)


## Running the tests

below is a typical maven command to trigger the test. The cucumber.options tag can be omitted to trigger all cucumber scenarios or set to trigger specific tests.   

Run with:
```bash
   mvn clean verify -P qa_build -Dcucumber.options="-t @Smoke"
```

### Configuration

There are two key config files:
* Maven's pom.xml file:  
  
Manages library dependency, plugins and used to set env URL, switch on/off Selenium grid and Browsermob web proxy capturing.

* TestNG's xml file (environment_configurations_to_test.xml):  
  
Manages various environment configurations (Operating system, browser,  browser version and headless mode) and parallel testing using multi-threading. 

### TestNG's xml file (environment_configurations_to_test.xml): 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Environment config tsts" parallel="tests"
	thread-count="3">


	<test name="windows chrome">
		<parameter name="operating_system" value="windows" />
		<parameter name="browser" value="chrome" />
		<parameter name="browser_headless" value="false" />
		<classes>
			<class name="integrationTests.cucumber.Runner" />
		</classes>
	</test>

	<test name="windows firefox">
		<parameter name="operating_system" value="windows" />
		<parameter name="browser" value="firefox" />
		<parameter name="browser_headless" value="false" />
		<classes>
			<class name="integrationTests.cucumber.Runner" />
		</classes>
	</test>

	<test name="windows edge">
		<parameter name="operating_system" value="windows" />
		<parameter name="browser" value="edge" />
		<parameter name="browser_headless" value="false" />
		<classes>
			<class name="integrationTests.cucumber.Runner" />
		</classes>
	</test>
	
</suite>
```

### Steps

* Maven triggers build. 
* Fail-safe plugin triggers testNG looking at TestNG's xml file. 
* TestNG's xml file is currently configured to run 3 env configs in parallel. 
* TestNG's xml file executes these tests by pointing to the runner class 
* The runner class triggers WebDriver creation (which is wrapped in a static thread Local variable) to allow for multi-threading.
* (WebDriver creation also supports various browser types, web proxy HTTP capture and headless mode).
* The runner class then loops through the cucumber scenarios using data provider annotation. 
* (Any failures are captured with Scenerio name, HTTP traffic and screenshot).
* The runner class (after testing) performs tear down and triggers report generation. 
* Masterthought reporter consolidates the results after each thread completes. 


### Selenium design pattern 

* Page object model (pom) classes   
A java class is created per page which contains the pages key DOM objects, and methods to manipulate the page.  

* Base class (Common_methods_and_pom)  
A base class stores lots of custom-made web driver methods, which are designed to make test scripts as robust as possible. One key aspect to this is the use of fluid waits and Ajax call waits prior to interacting with DOM elements.  
This base class also creates objects for all the page object model classes. When this class is inherited, all these objects can be utilized, which removes the need to create them within each Scenario.  

* Cucumber steps classes   
Are kept as abstract as possible, and inherit the base class, which enables them to call page object model objects, which in turn do most of the Selenium execution. They also have the option of utilizing any useful methods inherited from the base class.  

* Runner class   
Controls the test framework and combines testing with Cucumber. Java refraction is used so each env config test can generated a unique JSON file name. JSON files are consolidated by the Master Thoughts cucumber reporter. If refraction wasn't done, then a runner class would need to be created per env config test, to get unique JSON file names (since cucumber annotation cant be parameterized). This in my opinion isn't very fluid. Without unique JSON file names, the reporting would be corrupt.  

* WebDriver_factory class   
Creates static thread local WebDriver. This allows for unique WebDriver per thread, and allows for static reference which is simpler than having to pass around a WebDriver object throughout the framework. This class can generate Grid and non grid web drivers, and different operating systems and browsers. 


 
