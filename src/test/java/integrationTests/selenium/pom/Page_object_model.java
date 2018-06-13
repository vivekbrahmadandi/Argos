package integrationTests.selenium.pom;

import integrationTests.selenium.Selenium_core;

public class Page_object_model extends Selenium_core {

	//create POM objects
	protected static  POM_mainHeader mainHeader = new POM_mainHeader();
	protected static  POM_basket basket = new POM_basket();
	protected static  POM_productResults productResults = new POM_productResults();
	protected static  POM_categorySplashPage categorySplashPage = new POM_categorySplashPage();	
	protected static  POM_popup popup = new POM_popup();
	protected static  POM_productPage productPage = new POM_productPage();
	protected static  POM_popupBasket popupBasket = new POM_popupBasket();	
	protected static  POM_paypal paypal = new POM_paypal();		

}



