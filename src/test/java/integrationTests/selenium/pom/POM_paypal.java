package integrationTests.selenium.pom;

//JUNIT Test framework
import org.junit.Assert;

import org.openqa.selenium.By;

public class POM_paypal extends Page_object_model {

	//*[@id="confirmButtonTop"]

	private By txtEmail = By.id("email");
	private By txtPassword = By.id("password");
	private By btnLogin = By.id("btnLogin");
	private By btnNext = By.id("btnNext");	
	private By btnConfirm = By.id("confirmButtonTop");	
	private By loading = By.xpath("//div[@class=\"spinner ng-scope\"]");
	private By txtGivenName = By.xpath("//span[@class=\"given-name ng-binding\"]");
	
	private By btnCloseCart = By.id("closeCart");	

	private By btnHaveAccount = By.xpath("//a[@class=\"btn full ng-binding\"]");	
	
	
	//*[@id="loginSection"]/div/div[2]/a
	

	
	private By H1_retailersPage = By.xpath("//html/body/div/h1");	//usually has own POM file, but its only 1 object.
	
	public void login(String confirmURL, String username,String password) throws Exception{

		webdriver.get(confirmURL);
		
		waitForElement(txtEmail);
		
		
		//In case a different page is shown, close cart, and click button to get to login screen.
		if (elementExists(btnCloseCart)){
			click(btnCloseCart);
		}	
		if (elementExists(btnHaveAccount)){
			click(btnHaveAccount);
			
			//if previous button was clicked, then next page will have btnNext
			waitForElement(btnNext);
		}	
		
		
		sendkeys(txtEmail,username);

		//if the next button appears rather than password field, then click next
		if (elementExists(btnNext)){
						
			click(btnNext);
		}
		
		sendkeys(txtPassword,password);
		click(btnLogin);

	}
	
	public void confirmPayment() throws Exception{

		waitForElement(txtGivenName);
	
		waitForElementInvisible(loading);
		 //Issue with app, it reshows the loading screen, stopping click so fix delay required
		Thread.sleep(1500);
	
		if (elementExists(btnCloseCart)){
			click(btnCloseCart);
		}
		
		waitForElementInvisible(loading);
		 //Issue with app, it reshows the loading screen, stopping click so fix delay required
		Thread.sleep(1500);
		
		click(btnConfirm);

		waitForElement(H1_retailersPage);
		Assert.assertEquals(getText(H1_retailersPage), "Example Domain");
	
		System.out.println("###" + webdriver.getCurrentUrl());
		
			
	}	
	
}