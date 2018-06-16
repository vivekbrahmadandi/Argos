package integrationTests.selenium.main;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

public class WebDriver_control extends Selenium_core{

	private static String os_name = System.getProperty("os.name").toLowerCase();

	//==================================
	// Selenium Grid Enabled: will find node/s to match current environment_configurations_to_test.xml test 
	//==================================
	public static void createGridWebDriver(String selenium_grid_hub,String operatingSystem, String browserType,boolean browserHeadless) throws MalformedURLException {

		System.out.println(selenium_grid_hub);
		
		MutableCapabilities options = null;

		switch (browserType){

		case "chrome":
			
			options = new ChromeOptions();
			((ChromeOptions) options).setHeadless(browserHeadless);
			break;

		case "firefox":

			options = new FirefoxOptions();
			((FirefoxOptions) options).setHeadless(browserHeadless);
			break;

		case "edge":

			options = new EdgeOptions();
			break;

		}

		switch (operatingSystem){

		case "windows":

			options.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);	
			break;

		case "linux":

			options.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);
			break;

		case "mac":

			options.setCapability(CapabilityType.PLATFORM_NAME, Platform.MAC);
			break;

		}
		
		webdriver = new RemoteWebDriver(new URL(selenium_grid_hub + "/wd/hub"), options);
	
		setWebDriverWaitTime();

	}

	//==================================
	// Selenium Grid not Enabled: - will run on current machine. Will still attempt to execute all tests found in environment_configurations_to_test.xml however will skip if operating system doesnt match. 
	//==================================

	public static void createStandardWebDriver(String operatingSystem, String browserType,boolean browserHeadless) throws MalformedURLException {

		switch (operatingSystem){

		case "windows":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\windows\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\windows\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\windows\\MicrosoftWebDriver.exe");

			if (!(os_name.indexOf("win") >= 0)) {

				System.out.println("************");
				System.out.println("This test configuration is expecting WINDOWS, but this is " + os_name);
				System.out.println("************");	
				throw new SkipException("skipping test");
			}

			break;

		case "linux":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\linux\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\linux\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\linux\\MicrosoftWebDriver.exe");

			if (!(os_name.indexOf("nix") >= 0) || !(os_name.indexOf("nux") >= 0) || !(os_name.indexOf("aix") >= 0) ) {

				System.out.println("************");
				System.out.println("This test configuration is expecting LINUX, but this is " + os_name);
				System.out.println("************");	
				throw new SkipException("skipping test");
			}

			break;


		case "mac":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\mac\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\mac\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\mac\\MicrosoftWebDriver.exe");

			if (!(os_name.indexOf("mac") >= 0)) {

				System.out.println("************");
				System.out.println("This test configuration is expecting MAC, but this is " + os_name);
				System.out.println("************");	
				throw new SkipException("skipping test");
			}

			break;

		default: 

			System.out.println("===========================");
			System.out.println(operatingSystem + " is not a recognised operating system, please check config. Aborting test");
			System.out.println("===========================");
			throw new SkipException("skipping test");
		}


		switch(browserType){

		case "chrome":

			ChromeOptions options = new ChromeOptions();

			if (browserHeadless){

				options.addArguments("headless");
				webdriver = new ChromeDriver(options);
				webdriver.manage().window().setSize(new Dimension(1080, 1920));
			}else{

				webdriver = new ChromeDriver();
				webdriver.manage().window().maximize();
			}

			break;


		case "firefox":

			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

			if (browserHeadless){

				FirefoxBinary firefoxBinary = new FirefoxBinary();
				firefoxBinary.addCommandLineOptions("--headless");
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setBinary(firefoxBinary);
				webdriver = new FirefoxDriver(firefoxOptions);	
				webdriver.manage().window().setSize(new Dimension(1080, 1920));


			}else{

				webdriver = new FirefoxDriver();
				webdriver.manage().window().maximize();
			}
			break;

		case "edge":

			if (browserHeadless){

				System.out.println("===========================");
				System.out.println(browserType + " doesn't have a headless mode, launching normally");
				System.out.println("===========================");

			}	

			webdriver = new  EdgeDriver();
			webdriver.manage().window().maximize();

			break;

		default: 

			System.out.println("===========================");
			System.out.println(browserType + " is not a recognised web browser, please check config. Aborting test");
			System.out.println("===========================");
			throw new SkipException("skipping test");

		}

		setWebDriverWaitTime();

	}


	public static void setWebDriverWaitTime(){

		wait = new WebDriverWait(webdriver,60);
		quickWait = new WebDriverWait(webdriver, 1);

	}

	public static void quitWebDriver() throws Exception{

		webdriver.quit();

	}

}
