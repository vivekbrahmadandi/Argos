package integrationTests.selenium.page_object_model;

import org.openqa.selenium.By;

import cucumber.api.PendingException;

public class POM_productPage extends Page_object_model {

	public By linkProduct = By.xpath("//a[@class=\"ac-product-link ac-product-card__details\"]");
	public By txtProductPrice = By.xpath("//li[@class=\"price product-price-primary\"]");
	public By dropSort = By.xpath("//select[@class=\"font-standard form-control sort-select\"]");
	public By txtPostCode = By.xpath("//input[@class=\"form-control form-group__input fulfilment-search\"]");	
	public By btnCheckPostcode = By.xpath("//button[@class=\"button button--secondary button--quarter\"]");	
	public By btnAddBasketSmall = By.xpath("//button[@class=\" add-to-trolley-button button button--secondary button--quarter button--tiny\"]");	
	public By linkChangeStore = By.linkText("Change store");
	public By linkChangePostcode = By.linkText("Change postcode");
	
	public By txtAvail = By.xpath("//span[@class=\"availability-message message-tick  has-fast-track has-fast-track-clickable\"]");		
	public By txtPrice = By.xpath("//li[@itemprop=\"price\"]");		
	public By txtProductDescription = By.xpath("//div[@class=\"product-description-content-text\"]");	
	public By linkProductOwner = By.xpath("//a[@class=\"a\"]");		
	public By imgProduct = By.xpath("//img[contains(@style, 'transform:translate(0px, 0px)')]");	
	public By imgRelatedProduct = By.xpath("//img[@class=\"product-card__image\"]");				
	public By btnAddToBasket = By.xpath("//button[@class=\" add-to-trolley-button button button--primary button--full\"]");	

	public By loadingWheel = By.xpath("//div[@class=\"ac-loading-wheel ac-loading-wheel--contained\"]");	
	
	public void adds_product_to_basket_via_productPage() throws Throwable {

		popup.escPopup();
		
		scrollBottom();
		//scrollBy(2000,webdriver);

		if (!textExists("Not available online")){
			click(btnAddToBasket);
			popupBasket.checkContinueShopping();
		}else{
			System.out.println("This item is not available online to add to basket");
			throw new PendingException();
		}

	}
	
	public void change_store_location(String postcode) throws Throwable {

		sendkeys(txtPostCode,postcode);
		waitForElementInvisible(loadingWheel);
		click(btnCheckPostcode);
		waitForElementInvisible(btnCheckPostcode);
		
	}
	

}
