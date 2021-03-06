package integrationTests.selenium.main;

import java.net.*;

import org.openqa.selenium.*;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.opera.*;
import org.openqa.selenium.safari.*;
import org.openqa.selenium.ie.*;

import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.*;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;

import org.testng.SkipException;

public class WebDriver_factory {

	//Parallel processing achieved in testNG using ThreadLocal
	private static ThreadLocal<WebDriver> webdriver_threadLocal = new ThreadLocal<WebDriver>();

	//Relevent to getLocalThreadOS() - giving option to find out the threads OS
	private static ThreadLocal<String> operating_system_threadLocal = new ThreadLocal<String>();
	
	//Relevent to getLocalThreadBrowser() - giving option to find out the threads web browser
	private static ThreadLocal<String> browser_threadLocal = new ThreadLocal<String>();

	//Relevent to getLocalThreadBrowserMobProxyServer() - giving option to find out the threads BrowserMob Proxy server
	private static ThreadLocal<BrowserMobProxyServer> mobProxyServer_threadLocal = new ThreadLocal<BrowserMobProxyServer>();

	private static String os_name = System.getProperty("os.name").toLowerCase();

	@SuppressWarnings("deprecation")
	public static void setWebDriver(
			String operating_system, 
			String browser, 
			String browser_version,
			boolean browser_headless,
			boolean web_proxy,
			String selenium_grid_hub) throws MalformedURLException {

		browser_threadLocal.set(browser);
		operating_system_threadLocal.set(operating_system);

		WebDriver webdriver = null;
		MutableCapabilities options;

		//==================================
		// Selenium Grid not Enabled: - will run on current machine. Will still attempt to execute all tests found in environment_configurations_to_test.xml however will skip if operating system doesnt match. 
		//==================================

		if (selenium_grid_hub == null){

			if (!build_machine_supports_desired_OperatingSystem(operating_system)){

				System.out.println("************");
				System.out.println("[skipping test] This build machine does not support operating system: " + os_name);
				System.out.println("************");	
				throw new SkipException("skipping test");

			}

			setDriverProperty(operating_system);

			//Create browser specific webdriver with capabilities
			options = setBrowserCapabilities(browser , browser_headless, web_proxy);

			//Create the correct webdriver based on test requirements
			switch(browser){
			case "chrome": webdriver = new ChromeDriver((ChromeOptions)options); break;
			case "firefox": webdriver = new FirefoxDriver((FirefoxOptions)options); break;
			case "edge": webdriver = new EdgeDriver((EdgeOptions)options);break;
			}

		}else{

			//==================================
			// Selenium Grid Enabled: will find node/s to match current environment_configurations_to_test.xml test 
			//==================================

			//build browser options / capabilities
			options = setBrowserCapabilities(browser , browser_headless, web_proxy);

			//Set capabilityType, which is used to find grid node with matching capabilities
			options.setCapability(CapabilityType.BROWSER_NAME, browser);
			if (!browser_version.equals(""))options.setCapability(CapabilityType.BROWSER_VERSION, browser_version);
			if (!operating_system.equals(""))options.setCapability(CapabilityType.PLATFORM_NAME, operating_system);
			if (!operating_system.equals(""))options.setCapability(CapabilityType.PLATFORM, operating_system);

			//Launch Selenium grid, looking for node/s which match above capabilities
			webdriver = new RemoteWebDriver(new URL(selenium_grid_hub), options);

			System.out.println("Webdriver launched on node successfully for: " + operating_system + "/" + browser);

		}

		webdriver.manage().window().setSize(new Dimension(1080, 1920));
		webdriver.manage().window().maximize();

		webdriver_threadLocal.set(webdriver);

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


	public static String getLocalThreadOS() {

		return operating_system_threadLocal.get();

	}

	public static BrowserMobProxyServer getLocalThreadBrowserMobProxyServer() {

		return mobProxyServer_threadLocal.get();

	}
	
	
	private static void setDriverProperty(String operating_system){

		//Set driver property
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
	}

	private static MutableCapabilities setBrowserCapabilities(String browser, Boolean browser_headless, Boolean web_proxy){

		MutableCapabilities options;

		browser = browser.toLowerCase();

		switch (browser){

		case "chrome":    

			options = new ChromeOptions(); 
			((ChromeOptions) options).setAcceptInsecureCerts(true);

			if (browser_headless)((ChromeOptions) options).addArguments("headless");			

			break;

		case "firefox": 		

			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

			options = new FirefoxOptions(); 
			((FirefoxOptions) options).setAcceptInsecureCerts(true);

			if (browser_headless){

				FirefoxBinary firefoxBinary = new FirefoxBinary();
				firefoxBinary.addCommandLineOptions("--headless");
				((FirefoxOptions) options).setBinary(firefoxBinary);

			}

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

		default:
			System.out.println("===========================");
			System.out.println("[skipping test] " + browser + " is not a recognised web browser, please check config.");
			System.out.println("===========================");
			throw new SkipException("skipping test");	
		}

		//Create a browser proxy to capture HTTP data for analysis
		if (web_proxy){

			final BrowserMobProxyServer mobProxyServer = new BrowserMobProxyServer();

			mobProxyServer.setTrustAllServers(true);
			mobProxyServer.setHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
			mobProxyServer.start(0);

			Proxy seleniumProxy  = ClientUtil.createSeleniumProxy(mobProxyServer);

			options.setCapability(CapabilityType.PROXY, seleniumProxy);

			//System.out.println("Port started:" +  mobProxyServer.getPort());

			mobProxyServer.newHar(getLocalThreadOS() + "_" + getLocalThreadBrowser() + ".har");

			mobProxyServer_threadLocal.set(mobProxyServer);
			

		}
		
		return options;

	}
	

	private static boolean build_machine_supports_desired_OperatingSystem(String operating_system){

		//Check the current test config specified operating system matches build machine, if not then skip test.
		//If multiple OS testing is required then consider turning on Selenium Grid flag.

		boolean valid = false;

		if (operating_system.equals("windows") && os_name.indexOf("win") >= 0) valid = true;
		if (operating_system.equals("linux") && (os_name.indexOf("nix") >= 0 || os_name.indexOf("nux") >= 0 || os_name.indexOf("aix") >= 0)) valid = true;
		if (operating_system.equals("mac") && os_name.indexOf("mac") >= 0) valid = true;

		return valid;

	}

}
