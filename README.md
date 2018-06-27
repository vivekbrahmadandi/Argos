# Selenium BDD framework 

This framework enables parallel testing across multiple environment configurations then consolidates the results into a single Cucumber report. Here is an example: https://ibb.co/d7xc28.

## Authors

* **Peter Anderson (peter.x4000@gmail.com)** 

## Built With

* [Java](https://en.wikipedia.org/wiki/Java_(programming_language)) - Programming language
* [Selenium](https://en.wikipedia.org/wiki/Selenium_(software)) - Automates web applications
* [Cucumber-JVM](https://github.com/cucumber/cucumber-jvm) - Java implementation of Cucumber
* [Maven](https://en.wikipedia.org/wiki/Apache_Maven) - Build automation tool
* [TestNG](https://en.wikipedia.org/wiki/TestNG) - Testing framework
* [Browsermob web proxy](https://github.com/lightbody/browsermob-proxy) - Web proxy to capture HTTP content
* [Master Thoughts](https://mvnrepository.com/artifact/net.masterthought/cucumber-reporting) - Provides pretty html reports for Cucumber
* [SOAPUI](https://en.wikipedia.org/wiki/SoapUI) - Web service testing (SOAP and REST)


The Selenium tests are driven using cucumber BDD with the TestNG framework. 
Maven is used to build the project and the failsafe plugin is used to trigger TestNG, which in turn executes the Cucumber scenarios.  

this project also contains a REST services test using SOAPUI. 

below is a typical maven command to trigger the test. The cucumber.options tag can be omitted to trigger all cucumber scenarios or set to trigger specific tests.   

Run with:
```bash
   mvn clean verify -P qa_build -Dcucumber.options="-t @Smoke"
```

They key benefit of this framework, is its ability to test multiple environments configs within a single build. 
The environment configs are stored in a testng xml file. 

With this below configuration, each Cucumber scenario will be triggered in parallel against 3 different browsers.
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

### Selenium Grid

This framework also works with Selenium Grid. Within the maven pom.xml file, the tester can switch a flag to turn on/off selenium grid. 
With it off, the test will trigger against the current build machine. With it on, it will connect to the hub URL, which is also set in the maven pom.xml file. Naturally the hub and nodes need to be setup beforehand. When grid is enable this example framework points to saucelabs.com, which enables testing against a whole range of operating system, browsers, browser versions etc. 
Its suggest you make a free saucelabs account, and insert your own hub credentials into the framework to play with.  

### Reporting

This framework generates screenshot on failure and includes details of the cucumber scenario which failed as well as a HAR file. The HAR file contains all the HTTP data captured during the execution of the scenario, which is very useful for analysis. BrowserMob proxy is used to captured HTTP traffic.

After each environment config test has completed, the cucumber (Master Thoughts) report is updated. Upon completion, the tester can view the consolidated report. 

Cucumber generates json files which is what Master Thoughts uses to compile the data. The json file names need to be hard coded within the cucumber annotation, however this framework uses java reflection to dynamically rename the json files per environment config test. This is key because it means we only need one Cucumber runner class which can support any number of parallel tests. Unique json files are required otherwise you would get contention issues and lost results.   
