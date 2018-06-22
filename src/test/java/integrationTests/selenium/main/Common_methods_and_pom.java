//===================================
/* 
 	This is first class to be called by cucumber.
	It's annotated with @Before and modified so 
	it only runs the conditional code once before all tests.

	The conditional points to Selenium setup

 */
//===================================

package integrationTests.selenium.main;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.commons.io.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.ui.*;
import integrationTests.selenium.main.WebDriver_factory;
import integrationTests.selenium.page_object_model.POM_basket;
import integrationTests.selenium.page_object_model.POM_categorySplashPage;
import integrationTests.selenium.page_object_model.POM_mainHeader;
import integrationTests.selenium.page_object_model.POM_popup;
import integrationTests.selenium.page_object_model.POM_popupBasket;
import integrationTests.selenium.page_object_model.POM_productPage;
import integrationTests.selenium.page_object_model.POM_productResults;

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

			WebDriver_factory.getLocalThreadWebDriver().manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
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

		//Get second tab (child window)
		Set <String> allWindows =  WebDriver_factory.getLocalThreadWebDriver().getWindowHandles();

		//Only attempt to switch to RecentTab, if a new tab exists. 

		for(String wHandle: allWindows){

			//System.out.println(wHandle);

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
	// Take Screenshots
	//================================================

	public static void takeSnapShot(String filePath) throws Exception{

		//Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot =((TakesScreenshot)WebDriver_factory.getLocalThreadWebDriver());

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

		String netData = ((JavascriptExecutor)WebDriver_factory.getLocalThreadWebDriver()).executeScript(scriptToExecute).toString();
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
		System.out.println(scriptCounter + " scripts executed by " + WebDriver_factory.getLocalThreadWebDriver().getCurrentUrl());

	}	

	public boolean checkImageExists(By by) throws InterruptedException{

		waitForAjaxComplete();

		WebElement ImageFile = WebDriver_factory.getLocalThreadWebDriver().findElement(by);
		return  (Boolean) ((JavascriptExecutor)WebDriver_factory.getLocalThreadWebDriver()).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", ImageFile);

	}

}
