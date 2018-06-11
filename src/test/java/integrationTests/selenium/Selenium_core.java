//===================================
/* 
 	This is first class to be called by cucumber.
	It's annotated with @Before and modified so 
	it only runs the conditional code once before all tests.

	The conditional points to Selenium setup

 */
//===================================

package integrationTests.selenium;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

//import com.smartbear.ready.cmd.runner.SoapUITestCaseRunner;

public class Selenium_core{

	public static WebDriver webdriver;
	protected static WebDriverWait wait;

	static String os; 
	static String env; 
	static String browser; 
	static boolean browserHeadless; 		
	static String browserParallelCount; 

	
	//static blocks only called once 
	static{

		//===========================
		//Get system properties set within Maven using the maven-failsafe-plugin
		//Create webdriver to launch browser
		//===========================

		os = System.getProperty("os");
		env = System.getProperty("env");	
		browser = System.getProperty("browser");
		browserHeadless = Boolean.parseBoolean(System.getProperty("browser.headless"));
		browserParallelCount = System.getProperty("browser.parallel.count");

		System.out.println("BUILD CONFIGURATION");
		System.out.println("===========================");
		System.out.println("Operating system: " + os );		
		System.out.println("Web Browser: " + browser );	
		System.out.println("Browser headless mode: " + browserHeadless );	
		System.out.println("Browser parallel count: " + browserParallelCount );		
		System.out.println("===========================");

		createWebDriver();

	}

	public static void createWebDriver() {

		//===========================
		// Multi operating-system configuration
		//===========================v

		//set system properties for webdriver depending on OS.

		os = os.toLowerCase();

		switch (os){

		case "windows":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\windows\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\windows\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\windows\\MicrosoftWebDriver.exe");
			System.setProperty("webdriver.opera.driver",  System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\windows\\operadriver.exe");
			break;

		case "linux":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\linux\\");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\linux\\");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\linux\\");
			System.setProperty("webdriver.opera.driver",  System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\linux\\");
			break;


		case "mac":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\mac\\");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\mac\\");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\mac\\");
			System.setProperty("webdriver.opera.driver",  System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\mac\\");
			break;

		default: 

			System.out.println("===========================");
			System.out.println(os + " is not a recognised operating system, please check config. Aborting test");
			System.out.println("===========================");
			System.exit(0);
		}

		//===========================
		// Multi web browser configuration
		//===========================

		browser = browser.toLowerCase();

		switch(browser){

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

		case "edge":

			if (browserHeadless){

				System.out.println("===========================");
				System.out.println(browser + " doesn't have a headless mode, launching normally");
				System.out.println("===========================");
				
			}	
				
			webdriver = new  EdgeDriver();

			break;


		default: 
	
			System.out.println("===========================");
			System.out.println(browser + " is not a recognised web browser, please check config. Aborting test");
			System.out.println("===========================");
			System.exit(0);
	
		}


		wait = new WebDriverWait(webdriver,60);
		deleteCookies();

	}

	public static void quitWebDriver() throws Exception{
 
		webdriver.quit();

	}

	public void restartWebDriver() throws Exception{

		System.out.println("Restarting webdriver");	

		quitWebDriver();
		createWebDriver();

	}

	//===========================
	// Common operations
	//===========================

	public void gotoHomePage() throws Exception{

		webdriver.get(env);
		waitForAjaxComplete();

	}

	public int elementCount(By target) throws InterruptedException{

		waitForAjaxComplete();
		return webdriver.findElements(target).size();

	}	

	public boolean elementExists(By target) throws InterruptedException{

		waitForAjaxComplete();

		if (webdriver.findElements(target).size()>0){

			//[Fail-safe] check its clickable, to ensure it really does exist.
			try{
				WebDriverWait quickWait = new WebDriverWait(webdriver, 1);
				quickWait.until(ExpectedConditions.elementToBeClickable(target));

				return true;
			}
			catch (Exception e){
				return false;
			}
		}

		return false;

	}	

	public void waitForElement(By target) throws Exception{

		waitForAjaxComplete();
		wait.until(ExpectedConditions.visibilityOfElementLocated(target));

	}

	public void waitForElementInvisible(By target) throws Exception{

		wait.until(ExpectedConditions.invisibilityOfElementLocated(target));

	}

	//==================================================
	// Wait for DOM ready and Ajax calls on page to complete (Start)
	//==================================================

	public void waitForPageLoad() throws InterruptedException {

		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webdriver;

		int iWaitTime = 0;
		int iWaitFinish = 200;	

		while (!javascriptExecutor.executeScript("return document.readyState")
				.toString().equals("complete")) {

			Thread.sleep(500);
			iWaitTime++;

			System.out.println(iWaitTime + "/" + iWaitFinish + " Waiting for page to load (AJAX not included)");

			//fail-safe 
			if (iWaitTime==iWaitFinish){break;}
		}

	}

	public void waitForAjaxComplete() throws InterruptedException{

		waitForPageLoad();

		int iWaitTime = 0;
		int iWaitFinish = 200;

		JavascriptExecutor executor = (JavascriptExecutor)webdriver;

		if((Boolean) executor.executeScript("return window.jQuery != undefined")){
			while(!(Boolean) executor.executeScript("return jQuery.active == 0")){

				Thread.sleep(500);
				iWaitTime++;

				System.out.println(iWaitTime + "/" + iWaitFinish + " Waiting for active AJAX calls to complete");

				//fail-safe 
				if (iWaitTime==iWaitFinish){break;}        

			}
		}

	}

	//==================================================
	// Wait for DOM ready and Ajax calls on page to complete (End)
	//==================================================


	public boolean textExists(String text) {

		return webdriver.getPageSource().toLowerCase().contains(text.toLowerCase());


	}

	public void click(By target) throws Exception{


		wait.until(ExpectedConditions.elementToBeClickable(target)).click();

	}

	public void sendkeys(By target,String textToSend){

		wait.until(ExpectedConditions.visibilityOfElementLocated(target));

		try{

			//Clear text field if it has text before sending text.
			if(!webdriver.findElement(target).getAttribute("value").equals("") ||
					!webdriver.findElement(target).getText().equals("")){

				webdriver.findElement(target).clear();
			}

		}finally{
			webdriver.findElement(target).sendKeys(textToSend);	
		}	

	}

	public String getText(By target){

		return webdriver.findElement(target).getText();

	}	

	public String getInnerHTML(By target){

		return webdriver.findElement(target).getAttribute("innerHTML");

	}	


	public void selectByIndex(By target,int index){

		wait.until(ExpectedConditions.visibilityOfElementLocated(target));

		Select select = new Select(webdriver.findElement(target));
		select.selectByIndex(index);

	}


	public void selectByVisibleText(By target,String text) throws InterruptedException{

		wait.until(ExpectedConditions.visibilityOfElementLocated(target));

		Select select = new Select(webdriver.findElement(target));
		select.selectByVisibleText(text);

		//Poll until dropDown menu text changes to what we expect.
		int iWaitTime = 0;
		while(!getDropDownMenuText(target).contains(text)){
			Thread.sleep(500);
			iWaitTime++;

			System.out.println(iWaitTime + " polling element" + target);
			if (iWaitTime==10){break;}
		}	

	}

	public String getDropDownMenuText(By target) {

		wait.until(ExpectedConditions.visibilityOfElementLocated(target));

		Select select = new Select(webdriver.findElement(target));

		return select.getFirstSelectedOption().getText();

	}

	public void gotoNewTabIfExists(){

		String parentWindow;
		String childWindow;

		parentWindow = webdriver.getWindowHandle();
		childWindow = null;

		//Get second tab (child window)
		Set <String> allWindows =  webdriver.getWindowHandles();

		//Only attempt to switch to RecentTab, if a new tab exists. 

		for(String wHandle: allWindows){

			//System.out.println(wHandle);

			if (wHandle != parentWindow) {

				childWindow = wHandle;

			}
		}

		int attempts=1;
		if (!childWindow.equals(parentWindow)){
			while(webdriver.getWindowHandle().equals(parentWindow)) {
				webdriver.switchTo().window(childWindow);
				//Reporter.log("Switch window attempt:" +  attempts,true);
				attempts++;
			}
		}

	}	


	public static void deleteCookies(){

		webdriver.manage().deleteAllCookies();

	}

	//================================================
	// - Scrolling  (Start)
	//================================================


	public void scrollTo(By target) throws Exception {

		waitForElement(target);

		WebElement element = webdriver.findElement(target);
		((JavascriptExecutor) webdriver).executeScript("arguments[0].scrollIntoView(true);", element);
		//Thread.sleep(1500); 


	}

	public void scrollBy(int pixels) throws InterruptedException {

		((JavascriptExecutor) webdriver).executeScript("window.scrollBy(0," + pixels +")", "");
		Thread.sleep(1500);

	}


	public void scrollBottom() throws InterruptedException {

		((JavascriptExecutor) webdriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		Thread.sleep(1500);

	}

	public void scrollTop() throws InterruptedException {


		((JavascriptExecutor) webdriver).executeScript("window.scrollTo(0, 0)");
		Thread.sleep(1500);

	}	

	//================================================
	// - Scrolling  (End)
	//================================================

	public void mouseTo(By target) throws InterruptedException {

		wait.until(ExpectedConditions.elementToBeClickable(target));

		Actions action = new Actions(webdriver);
		action.moveToElement(webdriver.findElement(target)).build().perform();
		//Thread.sleep(1500);

	}	


	public void highLightElement(By by)  {

		WebElement we = webdriver.findElement(by);
		((JavascriptExecutor) webdriver).executeScript("arguments[0].style.border='3px dotted blue'", we);

	}	


	//================================================
	// Take Screenshots
	//================================================

	public static void takeSnapShot(String filePath) throws Exception{

		//Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot =((TakesScreenshot)webdriver);

		//Call getScreenshotAs method to create image file
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

		String fileName =  new SimpleDateFormat("yyyy-MM-dd_HHmmss'.png'").format(new Date());

		//Move image file to new destination
		File DestFile=new File(filePath + fileName);

		//Copy file at destination
		FileUtils.copyFile(SrcFile, DestFile);

		System.out.println("Screenshot on failure can be found here:");
		System.out.println(filePath + fileName);
	}


	public void getAllJS(){

		//String scriptToExecute = "return performance.getEntries({initiatorType : \"script\"});";
		String scriptToExecute = "return performance.getEntriesByType(\"resource\");";

		String netData = ((JavascriptExecutor)webdriver).executeScript(scriptToExecute).toString();
		String[] resourceNames = netData.split("name=");

		//========================================
		//=== Output resource details
		//========================================
		String[] _resourceNames = new String[resourceNames.length];

		System.out.println("==================================================");

		int scriptCounter = 0;

		for (int i=1;i<resourceNames.length;i++){

			if (resourceNames[i].contains("initiatorType=script")){

				_resourceNames[i] = resourceNames[i].split(", ")[0];
				System.out.println(_resourceNames[i]);
				scriptCounter++;
			}

		}
		System.out.println("==================================================");
		System.out.println(scriptCounter + " scripts executed by " + webdriver.getCurrentUrl());


	}	


	public boolean checkImageExists(By by){

		WebElement ImageFile = webdriver.findElement(by);
		return  (Boolean) ((JavascriptExecutor)webdriver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", ImageFile);

	}


	//=====================================================
	/* 
	  Method will invoke a SOAPUI project and return the log to calling method

	  Example use: 
	  Within SOAPUI you have a groovy step which saves a particular value using log.info
	  That value will be grabbed by this method and outputted. You may wish to then use that 
	  value in a selenium script. Thus this method allows for continous integration between
	  SOAPUI and Selenium/Java. 

	  //Call the SOAPUI project using my runAPI method, and it returns all the logs to Java
		List<String> logs = runAPI(dir + "PAYPAL_API.xml","PAYPAL_PAYMENT_TESTCASE","setPayment");

		//Show logs to console.
		for (String log: logs){
			System.out.println(log);
		}


	 */ 
	//=====================================================

	//	private SoapUITestCaseRunner runner;
	//	
	//	public void loadAPI(String projectXML){
	//
	//		runner = new SoapUITestCaseRunner(); 
	//		runner.setProjectFile(projectXML);
	//		runner.setPrintReport(true);
	//
	//	}
	//
	//	public List<String> runAPI(String setTestSuite, String setTestCase) {
	//
	//		ByteArrayOutputStream baos = null;
	//		PrintStream newPrintStream = null;
	//		PrintStream oldPrintStream = null;
	//
	//		try{
	//
	//			//==========================================================================
	//			//redirect console output so it can be saved to a string and queried later.
	//			//==========================================================================
	//
	//			// new stream to hold console output
	//			baos = new ByteArrayOutputStream();
	//			newPrintStream = new PrintStream(baos);
	//
	//			// Save the old stream
	//			oldPrintStream = System.out;
	//
	//			// Set Java to use new stream
	//			System.setOut(newPrintStream);
	//
	//			//==========================================================================
	//			//run SOAPUI test
	//			//==========================================================================
	//
	//			System.out.println("===== SOAP UI LOG (START) =====");
	//
	//			runner.setTestSuite(setTestSuite);
	//			runner.setTestCase(setTestCase);
	//
	//			runner.run();
	//
	//
	//		}catch(Exception e){
	//
	//			//If test fails, fail build, and show SOAPUI exception.
	//			Assert.fail(e.getMessage());
	//
	//			//finally block required to redirect Java back to old console output	
	//		}finally{
	//
	//			//==========================================================================
	//			//Save redirected output and put console output back to normal
	//			//==========================================================================
	//
	//			System.out.flush();
	//			System.setOut(oldPrintStream);
	//
	//			//==========================================================================
	//			//Show and then return the console output to calling method
	//			//==========================================================================
	//
	//			System.out.println(baos.toString());
	//
	//		}
	//
	//		List<String> logs = new ArrayList<String>();
	//
	//		String StringStart = "[log] ";
	//		String StringEnd = "\r";
	//
	//		Pattern p = Pattern.compile(Pattern.quote(StringStart) + "(.*?)" + Pattern.quote(StringEnd));
	//		Matcher m = p.matcher(baos.toString());
	//		while (m.find()) {
	//
	//			logs.add(m.group(1));
	//
	//		}
	//
	//		//if method is succesful, then SOAPUI logs will be pulled and return to calling method.
	//		return logs;
	//
	//	}	
	//
	//	//Use this to output to console all the logs returned by runAPI
	//	public void showRunAPIlog(List<String> logs) {
	//
	//		System.out.println("#### showRunAPIlog results below ####");	
	//
	//		for (String a: logs){
	//			System.out.println(a);	
	//		}
	//
	//	}
	//
	//	public void setAPIproperties(String...properties) {
	//
	//		//Example use: 
	//
	//		//setAPIproperties("access_token="+accessToken,"execute_url="+executeURL_API);
	//
	//		runner.setProjectProperties(properties); 
	//
	//
	//	}


}
