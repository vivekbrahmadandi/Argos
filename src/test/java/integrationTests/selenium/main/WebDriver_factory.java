package integrationTests.selenium.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;

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
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

public class WebDriver_factory {

	//Parallel processing achieved in testNG using ThreadLocal
	private static ThreadLocal<WebDriver> webdriver_threadLocal = new ThreadLocal<WebDriver>();
	
	//Relevent to getLocalThreadBrowser() - giving option to find out the threads web browser
	private static ThreadLocal<String> browser_threadLocal = new ThreadLocal<String>();

	
	private static String os_name = System.getProperty("os.name").toLowerCase();

	private MutableCapabilities options;

	//==================================
	// Selenium Grid Enabled: will find node/s to match current environment_configurations_to_test.xml test 
	//==================================
	@SuppressWarnings("deprecation")
	public void createLocalThreadGridWebDriver(String selenium_grid_hub,String operating_system, String browser, String browser_version,boolean browser_headless) throws MalformedURLException {

		//build browser options / capabilities
		options = setBrowserCapabilities(browser , browser_headless);

		//Set capabilityType, which is used to find grid node with matching capabilities
		options.setCapability(CapabilityType.BROWSER_NAME, browser);
		if (!browser_version.equals(""))options.setCapability(CapabilityType.BROWSER_VERSION, browser_version);
		if (!operating_system.equals(""))options.setCapability(CapabilityType.PLATFORM_NAME, operating_system);
		if (!operating_system.equals(""))options.setCapability(CapabilityType.PLATFORM, operating_system);

		//Launch Selenium grid, looking for node/s which match above capabilities
		WebDriver webdriver = new RemoteWebDriver(new URL(selenium_grid_hub), options);

		webdriver.manage().window().setSize(new Dimension(1080, 1920));
		webdriver.manage().window().maximize();

		webdriver_threadLocal.set(webdriver);
		browser_threadLocal.set(browser);

		System.out.println("Webdriver launched on node successfully");

	}

	//==================================
	// Selenium Grid not Enabled: - will run on current machine. Will still attempt to execute all tests found in environment_configurations_to_test.xml however will skip if operating system doesnt match. 
	//==================================

	public void createLocalThreadWebDriver(String operating_system, String browser,boolean browser_headless) throws MalformedURLException {

		if (!build_machine_supports_desired_OperatingSystem(operating_system)){
			
			System.out.println("************");
			System.out.println("[skipping test] This build machine does not support operating system: " + os_name);
			System.out.println("************");	
			throw new SkipException("skipping test");
			
		}
		
		//Set driver location
		switch(operating_system){
		
		case "windows":
			
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\windows\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\windows\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\windows\\MicrosoftWebDriver.exe");
			break;
			
		case "linux":
			
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\linux\\todo");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\linux\\todo");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\linux\\todo");
			break;
			
		case "mac":
		
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\mac\\todo");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\mac\\todo");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\mac\\todo");
			break;
			
		}


		//Create browser specific webdriver with capabilities
		options = setBrowserCapabilities(browser , browser_headless);

		WebDriver webdriver = null;
		
		//Create the correct webdriver based on test requirements
		switch(browser){

		case "chrome": webdriver = new ChromeDriver((ChromeOptions)options); break;
		case "firefox": webdriver = new FirefoxDriver((FirefoxOptions)options); break;
		case "edge": webdriver = new EdgeDriver((EdgeOptions)options);break;

		}

		webdriver.manage().window().setSize(new Dimension(1080, 1920));
		webdriver.manage().window().maximize();

		webdriver_threadLocal.set(webdriver);
		browser_threadLocal.set(browser);

	}

	
	public static void quitLocalWebDriver() throws Exception{

		if (webdriver_threadLocal.get()!= null) webdriver_threadLocal.get().quit();

	}

	public static WebDriver getLocalThreadWebDriver() {

		return webdriver_threadLocal.get();

	}
	

	public static String getLocalThreadBrowser() {

		return browser_threadLocal.get();

	}
	

	private MutableCapabilities setBrowserCapabilities(String browser, boolean browser_headless){

		MutableCapabilities options;

		browser = browser.toLowerCase();

		switch (browser){

		case "chrome":    

			options = new ChromeOptions(); 

			if (browser_headless)((ChromeOptions) options).addArguments("headless");			

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);

			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
			capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

			options.merge(capabilities);
			
			
			
			break;

		case "firefox": 		

			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

			options = new FirefoxOptions(); 

			if (browser_headless){

				FirefoxBinary firefoxBinary = new FirefoxBinary();
				firefoxBinary.addCommandLineOptions("--headless");

				((FirefoxOptions) options).setBinary(firefoxBinary);

			}

			break;

		case "edge":

			options = new EdgeOptions();

			if (browser_headless){

				System.out.println("===========================");
				System.out.println(browser + " doesn't have a headless mode, launching normally");
				System.out.println("===========================");

			}	

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

		default:
			System.out.println("===========================");
			System.out.println("[skipping test] " + browser + " is not a recognised web browser, please check config.");
			System.out.println("===========================");
			throw new SkipException("skipping test");	
		}

		return options;


	}

	
	private boolean build_machine_supports_desired_OperatingSystem(String operating_system){


		//Check the current test config specified operating system matches build machine, if not then skip test.
		//If multiple OS testing is required then consider turning on Selenium Grid flag.
		
		boolean valid = false;
		
		if (operating_system.equals("windows") && os_name.indexOf("win") >= 0) valid = true;
		if (operating_system.equals("linux") && (os_name.indexOf("nix") >= 0 || os_name.indexOf("nux") >= 0 || os_name.indexOf("aix") >= 0)) valid = true;
		if (operating_system.equals("mac") && os_name.indexOf("mac") >= 0) valid = true;
				

		return valid;
		
	}
	
	

}
