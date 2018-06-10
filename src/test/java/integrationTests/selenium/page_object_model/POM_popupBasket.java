package integrationTests.selenium.page_object_model;

import org.openqa.selenium.By;

public class POM_popupBasket extends Page_object_model {

	private By btnContinueShopping = By.xpath("//button[@class=\"button button--full button--secondary\"]");	
	private By loadingWheel = By.xpath("//div[@class=\"ac-loading-wheel ac-loading-wheel--contained\"]");
	
	public void checkContinueShopping() throws Exception{

		waitForElement(btnContinueShopping);
		
		waitForElementInvisible(loadingWheel);
		
		click(btnContinueShopping);

	}


}
