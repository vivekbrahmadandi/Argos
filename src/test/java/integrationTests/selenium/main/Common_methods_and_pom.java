package integrationTests.selenium.main;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.commons.io.*;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.logging.*;
import org.openqa.selenium.support.ui.*;
import integrationTests.selenium.page_object_model.*;

public class Common_methods_and_pom {

	public static String baseURL = System.getProperty("env.qa.url");

	private WebDriverWait wait;

	//===========================
	//create POM objects
	//===========================
	protected static POM_mainHeader mainHeader = new POM_mainHeader();
	protected static POM_basket basket = new POM_basket();
	protected static POM_productResults productResults = new POM_productResults();
	protected static POM_categorySplashPage categorySplashPage = new POM_categorySplashPage();	
	protected static POM_popup popup = new POM_popup();
	protected static POM_productPage productPage = new POM_productPage();
	protected static POM_popupBasket popupBasket = new POM_popupBasket();	


	public static String valueOf(){
		final String e = "e";

		return e;
	}

	//===========================
	// Common methods
	//===========================

	public void gotoPage(String url) throws Exception{

		WebDriver_factory.getLocalThreadWebDriver().get(url);
		waitForAjaxComplete();

	}

	public void navigateBack() throws Exception{

		WebDriver_factory.getLocalThreadWebDriver().navigate().back();
		waitForAjaxComplete();

	}

	public int elementCount(By target) throws InterruptedException {

		waitForAjaxComplete();
		return WebDriver_factory.getLocalThreadWebDriver().findElements(target).size();

	}	

	public List<WebElement> getAllElements(By target) throws InterruptedException {

		waitForAjaxComplete();
		return  WebDriver_factory.getLocalThreadWebDriver().findElements(target);

	}	

	public boolean elementExists(By target) throws InterruptedException{

		waitForAjaxComplete();

		if (WebDriver_factory.getLocalThreadWebDriver().findElements(target).size()>0){

			//[Fail-safe] check its clickable, to ensure it really does exist.
			try{

				WebDriverWait quickWait = new WebDriverWait(WebDriver_factory.getLocalThreadWebDriver(), 1);

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

			wait = new WebDriverWait(WebDriver_factory.getLocalThreadWebDriver(),60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(target));
		}
		catch (Exception e){
			System.out.println("Selenium has waiting its max time for the following element to be visible" + target );	
		}
	}

	public void waitForElementInvisible(By target) throws Exception{

		waitForAjaxComplete();

		try{
			wait = new WebDriverWait(WebDriver_factory.getLocalThreadWebDriver(),60);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(target));
		}catch(Exception e){

			System.out.println("Selenium has waiting its max time for the following element to not be visible" + target );	
		}

	}

	public void waitForElementNotClickable(By target) throws Exception{

		waitForAjaxComplete();

		try{
			wait = new WebDriverWait(WebDriver_factory.getLocalThreadWebDriver(),60);
			wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(target)));
		}catch(Exception e){

			System.out.println("Selenium has waiting its max time for the following element to not be clickable: " + target );	
		}

	}

	//==================================================
	// Wait for DOM ready and Ajax calls on page to complete (Start)
	//==================================================

	public static void waitForPageLoad() throws InterruptedException {

		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) WebDriver_factory.getLocalThreadWebDriver();

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

			WebDriver_factory.getLocalThreadWebDriver().manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
			((JavascriptExecutor) WebDriver_factory.getLocalThreadWebDriver()).executeAsyncScript(
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

			//System.out.println("Selenium_core.waitForAjaxComplete() threw: " + e.getMessage());

			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime); 
//			System.out.println("waiting for AJAX took: " + duration + "MS on URL: " 
//					+ WebDriver_factory.getLocalThreadWebDriver().getCurrentUrl());

		}

	}

	//==================================================
	// Wait for DOM ready and Ajax calls on page to complete (End)
	//==================================================

	public boolean textExists(String text) throws InterruptedException {

		waitForAjaxComplete();

		return WebDriver_factory.getLocalThreadWebDriver().getPageSource().toLowerCase().contains(text.toLowerCase());

	}

	public void click(By target) throws Exception{

		waitForElement(target);

		try{
			wait = new WebDriverWait(WebDriver_factory.getLocalThreadWebDriver(),60);
			wait.until(ExpectedConditions.elementToBeClickable(target));
		}catch(Exception e){
			System.out.println("Selenium has waiting its max time for the following element to be clickable: " + target );	
		}finally{
			WebDriver_factory.getLocalThreadWebDriver().findElement(target).click();
		}

	}

	public void sendkeys(By target,String textToSend) throws Exception{

		waitForElement(target);

		try{

			//Clear text field if it has text before sending text.
			if(!WebDriver_factory.getLocalThreadWebDriver().findElement(target).getAttribute("innerHTML").equals("") ||
					!WebDriver_factory.getLocalThreadWebDriver().findElement(target).getText().equals("")){

				WebDriver_factory.getLocalThreadWebDriver().findElement(target).clear();
			}

		}finally{
			WebDriver_factory.getLocalThreadWebDriver().findElement(target).sendKeys(textToSend);	
		}	

	}

	public String getText(By target) throws Exception{

		waitForElement(target);

		return WebDriver_factory.getLocalThreadWebDriver().findElement(target).getText();

	}	

	public String getInnerHTML(By target) throws Exception{

		waitForElement(target);

		return WebDriver_factory.getLocalThreadWebDriver().findElement(target).getAttribute("innerHTML");

	}	

	public void selectByIndex(By target,int index) throws Exception{

		waitForElement(target);

		Select select = new Select(WebDriver_factory.getLocalThreadWebDriver().findElement(target));
		select.selectByIndex(index);

		waitForAjaxComplete();

	}

	public void selectByVisibleText(By target,String text) throws Exception{

		waitForElement(target);

		Select select = new Select(WebDriver_factory.getLocalThreadWebDriver().findElement(target));
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

		Select select = new Select(WebDriver_factory.getLocalThreadWebDriver().findElement(target));

		return select.getFirstSelectedOption().getText();

	}

	public void gotoNewTabIfExists(){

		String parentWindow;
		String childWindow;

		parentWindow = WebDriver_factory.getLocalThreadWebDriver().getWindowHandle();
		childWindow = null;

		Set <String> allWindows =  WebDriver_factory.getLocalThreadWebDriver().getWindowHandles();

		//Only attempt to switch to RecentTab, if a new tab exists. 
		for(String wHandle: allWindows){

			if (wHandle != parentWindow) {

				childWindow = wHandle;
			}
		}

		int attempts=1;
		if (!childWindow.equals(parentWindow)){
			while(WebDriver_factory.getLocalThreadWebDriver().getWindowHandle().equals(parentWindow)) {
				WebDriver_factory.getLocalThreadWebDriver().switchTo().window(childWindow);
				//Reporter.log("Switch window attempt:" +  attempts,true);
				attempts++;
			}
		}

	}	

	public static void deleteCookies(){

		if (WebDriver_factory.getLocalThreadWebDriver().getCurrentUrl().equals("data:,") || 
				WebDriver_factory.getLocalThreadWebDriver().getCurrentUrl().equals("about:blank")){

			return;
		}

		WebDriver_factory.getLocalThreadWebDriver().manage().deleteAllCookies();

	}

	//================================================
	// - Scrolling  (code Start)
	//================================================

	public void scrollTo(By target) throws Exception {

		WebElement element = WebDriver_factory.getLocalThreadWebDriver().findElement(target);
		((JavascriptExecutor) WebDriver_factory.getLocalThreadWebDriver()).executeScript("arguments[0].scrollIntoView(true);", element);

		waitForAjaxComplete();

	}

	public void scrollBy(int pixels) throws InterruptedException {

		((JavascriptExecutor) WebDriver_factory.getLocalThreadWebDriver()).executeScript("window.scrollBy(0," + pixels +")", "");
		waitForAjaxComplete();

	}

	public void scrollBottom() throws InterruptedException {

		((JavascriptExecutor) WebDriver_factory.getLocalThreadWebDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		waitForAjaxComplete();

	}

	public void scrollTop() throws InterruptedException {

		((JavascriptExecutor) WebDriver_factory.getLocalThreadWebDriver()).executeScript("window.scrollTo(0, 0)");
		waitForAjaxComplete();

	}	

	//================================================
	// - Scrolling  (code End)
	//================================================

	public void mouseTo(By target) throws Exception {

		waitForElement(target);

		Actions action = new Actions(WebDriver_factory.getLocalThreadWebDriver());
		action.moveToElement(WebDriver_factory.getLocalThreadWebDriver().findElement(target)).build().perform();
		waitForAjaxComplete();

	}	

	public void highLightElement(By by)  {

		WebElement we = WebDriver_factory.getLocalThreadWebDriver().findElement(by);
		((JavascriptExecutor) WebDriver_factory.getLocalThreadWebDriver()).executeScript("arguments[0].style.border='3px dotted blue'", we);

	}	

	//================================================
	// Save Screenshots and log info (includes HTTP response code)
	//================================================

	public static void takeSnapShotAndLogs(String scenarioName) throws Exception{

		String browser = WebDriver_factory.getLocalThreadBrowser();
		String operatingSystem = WebDriver_factory.getLocalThreadOS();

		//Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot =((TakesScreenshot)WebDriver_factory.getLocalThreadWebDriver());

		//Call getScreenshotAs method to create image file
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

		String currentDateTime =  new SimpleDateFormat("yyyy-MM-dd_HHmm").format(new Date());
		
		String filePath = System.getProperty("user.dir").replace("\\", "/")  + 
						  "/target/screenshots_logs_on_failure/" + 
						  operatingSystem + "-" + browser + "_" + currentDateTime; 
		
		
		String screenshotPath = filePath + "/" + "screenshot.png";

		File DestFile=new File(screenshotPath);

		//Copy file at destination
		FileUtils.copyFile(SrcFile, DestFile);

		System.out.println("");
		System.out.println("Scenario Failed: " + scenarioName);
		System.out.println("Environment: " + WebDriver_factory.getLocalThreadOS() + "_" + WebDriver_factory.getLocalThreadBrowser());
		System.out.println("Screenshot ands logs found here: ");		
		System.out.println(filePath);
		System.out.println("");

		//Output LOGS to text file alongside the screenshot (only works for Chrome)
		File logFile = new File(filePath + "/" + "logs.txt");
		FileWriter fw = new FileWriter(logFile, false);
		BufferedWriter bw = new BufferedWriter(fw);

		if (browser.equals("chrome")) {

			try{

				LogEntries logs = WebDriver_factory.getLocalThreadWebDriver().manage().logs().get("performance");

				bw.write("Failed Scenario: " + scenarioName + System.lineSeparator() + System.lineSeparator()); 	
				
				for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();)
				{
					LogEntry entry = it.next();

					JSONObject json = new JSONObject(entry.getMessage());
					JSONObject message = json.getJSONObject("message");
					String method = message.getString("method");

					if (method != null
							&& "Network.responseReceived".equals(method)
							)
					{
						JSONObject params = message.getJSONObject("params");
						JSONObject response = params.getJSONObject("response");
						String messageUrl = response.getString("url");

						int status = response.getInt("status");

						try {

							bw.write("(HTTP " + status  + ") " +  messageUrl + System.lineSeparator() + 
									"headers: " + response.get("headers") + System.lineSeparator() + System.lineSeparator());

						} catch (IOException e) {
							e.printStackTrace();
						}    
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally{

				bw.close();
				fw.close();	

			}

		}else{

			try {
	
				bw.write("Failed Scenario: " + scenarioName + System.lineSeparator() + System.lineSeparator()); 	
				bw.write("HTTP logs only available with Chrome browser");
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally{

				bw.close();
				fw.close();	

			}  
		}

	}

	public void getAllJS() throws InterruptedException{

		waitForAjaxComplete();

		//String scriptToExecute = "return performance.getEntries({initiatorType : \"script\"});";
		String scriptToExecute = "return performance.getEntriesByType(\"resource\");";

		String netData = ((JavascriptExecutor)WebDriver_factory.getLocalThreadWebDriver()).executeScript(scriptToExecute).toString();
		String[] resourceNames = netData.split("name=");

		//========================================
		// Output resource details
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
		System.out.println(scriptCounter + " scripts executed by " + WebDriver_factory.getLocalThreadWebDriver().getCurrentUrl());

	}	

	public boolean checkImageExists(By by) throws InterruptedException{

		waitForAjaxComplete();

		WebElement ImageFile = WebDriver_factory.getLocalThreadWebDriver().findElement(by);
		return  (Boolean) ((JavascriptExecutor)WebDriver_factory.getLocalThreadWebDriver()).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", ImageFile);

	}

}
