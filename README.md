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
  
Manages various environment configurations (Operating system, browser and browser version) and parallel testing using multi-threading. 

### Steps

* Maven triggers build. 
* Fail-safe plugin triggers testNG looking at TestNG's xml file. 
* TestNG's xml file is currently configured to run 3 env configs in parallel. 
* TestNG's xml file executes these tests by pointing to the runner class 
* The runner class triggers WebDriver creation (which is wrapped in a static thread Local variable) to allow for multi-threading.
* (WebDriver creation also supports various browser types, web proxy HTTP capture and headless mode).
* The runner class then loops through the cucumber scenarios using data provider anotation. 
* (Any failures are captured with Scenerio name, HTTP traffic and screenshot).
* The runner class (after testing) performs tear down and triggers report generation. 
* Masterthought reporter consolidates the results after each thread completes. 


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

### Selenium framework




 
