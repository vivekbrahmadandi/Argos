//===================================
/* 
 	This is first class to be called by cucumber.
	It's annotated with @Before and modified so 
	it only runs the conditional code once before all tests.

	The conditional points to Selenium setup

 */
//===================================

package integrationTests.selenium;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.commons.io.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

//import com.smartbear.ready.cmd.runner.SoapUITestCaseRunner;

public class Selenium_core {

	public static String baseURL = System.getProperty("env.qa.url");
	
	public static WebDriver webdriver;
	protected static WebDriverWait wait;
	protected static WebDriverWait quickWait;
	
	private static String os_this_system = System.getProperty("os.name").toLowerCase();

	public static void createGridWebDriver(String selenium_grid_hub,String operatingSystem, String browserType,boolean browserHeadless) throws MalformedURLException {

		ChromeOptions options = new ChromeOptions();
		options.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
		options.addArguments("disable-infobars");
		options.addArguments("--no-default-browser-check");
		options.setAcceptInsecureCerts(true);
		options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
		options.setHeadless(false);

		webdriver = new RemoteWebDriver(new URL(selenium_grid_hub + "/wd/hub"), options);

		setWebDriverWaitTime();

	}

	public static void createStandardWebDriver(String operatingSystem, String browserType,boolean browserHeadless) throws MalformedURLException {
		
		switch (operatingSystem){

		case "windows":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\windows\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\windows\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\windows\\MicrosoftWebDriver.exe");
			
			if (!(os_this_system.indexOf("win") >= 0)) {
				
				System.out.println("************");
				System.out.println("************");
				Assert.fail("This test configuration is expecting WINDOWS, but this is a " + os_this_system);
				
			}
					
			break;

		case "linux":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\linux\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\linux\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\linux\\MicrosoftWebDriver.exe");
			
			if (!(os_this_system.indexOf("nix") >= 0) || !(os_this_system.indexOf("nux") >= 0) || !(os_this_system.indexOf("aix") >= 0) ) {
				
				System.out.println("************");
				System.out.println("************");
				Assert.fail("This test configuration is expecting LINUX, but this is a " + os_this_system);
				
			}
			
			break;


		case "mac":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\browser_drivers\\mac\\chromedriver.exe");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")  + "\\src\\test\\resources\\browser_drivers\\mac\\geckodriver.exe");
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")   + "\\src\\test\\resources\\browser_drivers\\mac\\MicrosoftWebDriver.exe");
			
			if (!(os_this_system.indexOf("mac") >= 0)) {
				
				System.out.println("************");
				System.out.println("************");
				Assert.fail("This test configuration is expecting MAC, but this is a " + os_this_system);
				
			}
			
			break;

		default: 

			System.out.println("===========================");
			System.out.println(operatingSystem + " is not a recognised operating system, please check config. Aborting test");
			System.out.println("===========================");
			System.exit(0);
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
			System.exit(0);

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

	//===========================
	// Common operations
	//===========================

	public void gotoPage(String url) throws Exception{
		
		webdriver.get(url);
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

		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(target));
		}
		catch (Exception e){
			System.out.println("Selenium has waiting its max time for the following element to be visible" + target );	
		}
	}

	public void waitForElementInvisible(By target) throws Exception{

		waitForAjaxComplete();

		try{
			wait.until(ExpectedConditions.invisibilityOfElementLocated(target));
		}catch(Exception e){

			System.out.println("Selenium has waiting its max time for the following element to not be visible" + target );	
		}

	}

	public void waitForElementNotClickable(By target) throws Exception{

		waitForAjaxComplete();

		try{
			wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(target)));
		}catch(Exception e){

			System.out.println("Selenium has waiting its max time for the following element to not be clickable: " + target );	
		}

	}

	//==================================================
	// Wait for DOM ready and Ajax calls on page to complete (Start)
	//==================================================

	public static void waitForPageLoad() throws InterruptedException {

		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webdriver;

		int iWaitTime = 0;
		int iWaitFinish = 200;	

		while (!javascriptExecutor.executeScript("return document.readyState")
				.toString().equals("complete")) {

			Thread.sleep(500);
			iWaitTime++;

			//System.out.println(iWaitTime + "/" + iWaitFinish + " Waiting for page to load (AJAX not included)");

			//fail-safe 
			if (iWaitTime==iWaitFinish){break;}
		}

	}


	public static void waitForAjaxComplete() throws InterruptedException {

		long startTime = System.currentTimeMillis();

		waitForPageLoad(); 


		try{

			webdriver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
			((JavascriptExecutor) webdriver).executeAsyncScript(
					"var callback = arguments[arguments.length - 1];" +
							"var xhr = new XMLHttpRequest();" +
							"xhr.open('POST', '/" + "Ajax_call" + "', true);" +
							"xhr.onreadystatechange = function() {" +
							"  if (xhr.readyState == 4) {" +
							"    callback(xhr.responseText);" +
							"  }" +
							"};" +
					"xhr.send();");

		}catch(Exception e){

			System.out.println("Selenium_core.waitForAjaxComplete() threw: " + e.getMessage());

			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime); 
			System.out.println("waiting for AJAX took: " + duration + "MS");

		}

	}

	//==================================================
	// Wait for DOM ready and Ajax calls on page to complete (End)
	//==================================================

	public boolean textExists(String text) throws InterruptedException {

		waitForAjaxComplete();

		return webdriver.getPageSource().toLowerCase().contains(text.toLowerCase());

	}

	public void click(By target) throws Exception{

		waitForElement(target);

		try{
			wait.until(ExpectedConditions.elementToBeClickable(target));
		}catch(Exception e){
			System.out.println("Selenium has waiting its max time for the following element to be clickable: " + target );	
		}finally{
			webdriver.findElement(target).click();
		}

	}

	public void sendkeys(By target,String textToSend) throws Exception{

		waitForElement(target);

		try{

			//Clear text field if it has text before sending text.
			if(!webdriver.findElement(target).getAttribute("innerHTML").equals("") ||
					!webdriver.findElement(target).getText().equals("")){

				webdriver.findElement(target).clear();
			}

		}finally{
			webdriver.findElement(target).sendKeys(textToSend);	
		}	


	}

	public String getText(By target) throws Exception{

		waitForElement(target);

		return webdriver.findElement(target).getText();

	}	

	public String getInnerHTML(By target) throws Exception{

		waitForElement(target);

		return webdriver.findElement(target).getAttribute("innerHTML");

	}	

	public void selectByIndex(By target,int index) throws Exception{

		waitForElement(target);

		Select select = new Select(webdriver.findElement(target));
		select.selectByIndex(index);

		waitForAjaxComplete();

	}

	public void selectByVisibleText(By target,String text) throws Exception{

		waitForElement(target);

		Select select = new Select(webdriver.findElement(target));
		select.selectByVisibleText(text);

		waitForAjaxComplete();

		//[Fail-safe] Poll until dropDown menu text changes to what we expect.
		int iWaitTime = 0;
		while(!getDropDownMenuText(target).contains(text)){
			Thread.sleep(500);
			iWaitTime++;

			//System.out.println(iWaitTime + " polling element" + target);
			if (iWaitTime==10){break;}
		}	

	}

	public String getDropDownMenuText(By target) throws Exception {

		waitForElement(target);

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

		if (webdriver.getCurrentUrl().equals("data:,") || 
				webdriver.getCurrentUrl().equals("about:blank")){

			return;
		}

		webdriver.manage().deleteAllCookies();


		//[Fail-Safe] make sure cookies cleared
		//		((JavascriptExecutor) webdriver).executeScript("window.sessionStorage.clear();");
		//		((JavascriptExecutor) webdriver).executeScript("window.localStorage.clear();");

	}

	//================================================
	// - Scrolling  (Start)
	//================================================

	public void scrollTo(By target) throws Exception {

		WebElement element = webdriver.findElement(target);
		((JavascriptExecutor) webdriver).executeScript("arguments[0].scrollIntoView(true);", element);

		waitForAjaxComplete();

	}

	public void scrollBy(int pixels) throws InterruptedException {

		((JavascriptExecutor) webdriver).executeScript("window.scrollBy(0," + pixels +")", "");
		waitForAjaxComplete();

	}

	public void scrollBottom() throws InterruptedException {

		((JavascriptExecutor) webdriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		waitForAjaxComplete();

	}

	public void scrollTop() throws InterruptedException {

		((JavascriptExecutor) webdriver).executeScript("window.scrollTo(0, 0)");
		waitForAjaxComplete();

	}	

	//================================================
	// - Scrolling  (End)
	//================================================

	public void mouseTo(By target) throws Exception {

		waitForElement(target);

		Actions action = new Actions(webdriver);
		action.moveToElement(webdriver.findElement(target)).build().perform();
		waitForAjaxComplete();

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

	public void getAllJS() throws InterruptedException{

		waitForAjaxComplete();

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

	public boolean checkImageExists(By by) throws InterruptedException{

		waitForAjaxComplete();

		WebElement ImageFile = webdriver.findElement(by);
		return  (Boolean) ((JavascriptExecutor)webdriver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", ImageFile);

	}

}
