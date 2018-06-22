package integrationTests.selenium.main;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

public class WebDriver_factory extends Selenium_core{

	private static WebDriver webdriver_threadLocal;
	private static WebDriverWait wait_threadLocal;
	private static WebDriverWait quickWait_threadLocal;
	
	private static String os_name = System.getProperty("os.name").toLowerCase();

	//==================================
	// Selenium Grid Enabled: will find node/s to match current environment_configurations_to_test.xml test 
	//==================================
	@SuppressWarnings("deprecation")
	public static void createGridWebDriver(String selenium_grid_hub,String operating_system, String browser, String browser_version,boolean browser_headless) throws MalformedURLException {

		MutableCapabilities options = null;

		//Create the correct browser object based on test requirements

		browser = browser.toLowerCase();

		switch (browser){

		case "chrome":
			options = new ChromeOptions();
			break;

		case "firefox":
			options = new FirefoxOptions();
			break;

		case "edge":
			options = new EdgeOptions();
			break;

		case "internet explorer":
			options = new InternetExplorerOptions();
			break;

		case "safari":
			options = new SafariOptions();
			break;		

		case "opera":
			options = new OperaOptions();
			break;

			//Browser needs to be specified in order to create the correct Options object 	
		default:
			System.out.println("===========================");
			System.out.println("[skipping test] " + browser + " is not a recognised web browser, please check config.");
			System.out.println("===========================");
			throw new SkipException("skipping test");	
		}

		//Set desired capabilities based on the input provided from environment_configurations_to_test.xml 
		options.setCapability(CapabilityType.BROWSER_NAME, browser);
		if (!browser_version.equals(""))options.setCapability(CapabilityType.BROWSER_VERSION, browser_version);
		if (!operating_system.equals(""))options.setCapability(CapabilityType.PLATFORM_NAME, operating_system);
		if (!operating_system.equals(""))options.setCapability(CapabilityType.PLATFORM, operating_system);

		//Launch Selenium grid, looking for node/s which match above capabilities
		webdriver_threadLocal = new RemoteWebDriver(new URL(selenium_grid_hub), options);
		
		System.out.println("Webdriver launched on node successfully");
		webdriver.set(webdriver_threadLocal);
		
		webdriver.get().manage().window().maximize();

		setWebDriverWaitTime();

	}

	//==================================
	// Selenium Grid not Enabled: - will run on current machine. Will still attempt to execute all tests found in environment_configurations_to_test.xml however will skip if operating system doesnt match. 
	//==================================

	public static void createStandardWebDriver(String operating_system, String browser,boolean browser_headless) throws MalformedURLException {

		//Check the current test config specified operating system matches build machine, if not then skip test.
		//If multiple OS testing is required then consider turning on Selenium Grid flag.
		switch (operating_system){

		case "windows":

			if (!(os_name.indexOf("win") >= 0)) {

				System.out.println("************");
				System.out.println("[skipping test] This test configuration is expecting WINDOWS, but this build machine is " + os_name);
				System.out.println("************");	
				throw new SkipException("skipping test");
			}
			
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\windows\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\windows\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\windows\\MicrosoftWebDriver.exe");


			break;

		case "linux":


			//Check if the build machine is linux, otherwise a linux test is not possible, so skip.  
			if (!(os_name.indexOf("nix") >= 0) || !(os_name.indexOf("nux") >= 0) || !(os_name.indexOf("aix") >= 0) ) {

				System.out.println("************");
				System.out.println("[skipping test] This test configuration is expecting LINUX, but this build machine is " + os_name);
				System.out.println("************");	
				throw new SkipException("skipping test");
			}

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\linux\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\linux\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\linux\\MicrosoftWebDriver.exe");

			
			break;


		case "mac":

			//Check if the build machine is mac, otherwise a mac test is not possible, so skip. 
			if (!(os_name.indexOf("mac") >= 0)) {

				System.out.println("************");
				System.out.println("[skipping test] This test configuration is expecting MAC, but this build machine is " + os_name);
				System.out.println("************");	
				throw new SkipException("skipping test");
			}

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\mac\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\mac\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\mac\\MicrosoftWebDriver.exe");

			break;

		default: 

			System.out.println("===========================");
			System.out.println("[skipping test] " +  operating_system + " is not a recognised operating system, please check config.");
			System.out.println("===========================");
			throw new SkipException("skipping test");
		}

		//Create the correct browser object based on test requirements
		
		switch(browser){

		case "chrome":

			ChromeOptions options = new ChromeOptions();

			if (browser_headless){

				options.addArguments("headless");
				
				webdriver_threadLocal = new ChromeDriver(options);
				webdriver.set(webdriver_threadLocal);
	
	
				webdriver.get().manage().window().setSize(new Dimension(1080, 1920));
			}else{

				webdriver_threadLocal = new ChromeDriver(options);
				webdriver.set(webdriver_threadLocal);
				webdriver.get().manage().window().maximize();
			}

			break;

		case "firefox":

			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

			if (browser_headless){

				FirefoxBinary firefoxBinary = new FirefoxBinary();
				firefoxBinary.addCommandLineOptions("--headless");
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setBinary(firefoxBinary);
				
				
				webdriver_threadLocal = new FirefoxDriver(firefoxOptions);
				webdriver.set(webdriver_threadLocal);
				webdriver.get().manage().window().setSize(new Dimension(1080, 1920));


			}else{

				webdriver_threadLocal = new FirefoxDriver();
				webdriver.set(webdriver_threadLocal);
	
				webdriver.get().manage().window().maximize();
			}
			break;

		case "edge":

			if (browser_headless){

				System.out.println("===========================");
				System.out.println(browser + " doesn't have a headless mode, launching normally");
				System.out.println("===========================");

			}	
			
			
			webdriver_threadLocal = new EdgeDriver();
			webdriver.set(webdriver_threadLocal);
			
			webdriver.get().manage().window().maximize();

			break;

		default: 

			System.out.println("===========================");
			System.out.println("[skipping test] " + browser + " is not a recognised web browser, please check config.");
			System.out.println("===========================");
			throw new SkipException("skipping test");

		}

		setWebDriverWaitTime();

	}


	public static void setWebDriverWaitTime(){

		wait_threadLocal = new WebDriverWait(webdriver.get(),60);
		wait.set(wait_threadLocal);
		
		quickWait_threadLocal = new WebDriverWait(webdriver.get(), 1);
		quickWait.set(quickWait_threadLocal);

	}

	public static void quitWebDriver() throws Exception{

		if (webdriver.get()!= null){

			webdriver.get().quit();

		}

	}

}
