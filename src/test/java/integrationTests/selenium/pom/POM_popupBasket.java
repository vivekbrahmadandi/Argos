package integrationTests.selenium.pom;

import org.openqa.selenium.By;

public class POM_popupBasket extends Page_object_model {

	private By btnContinueShopping = By.xpath("//button[@class=\"button button--full button--secondary\"]");	
	private By loadingWheel = By.xpath("//div[@class=\"ac-loading-wheel ac-loading-wheel--contained\"]");
	
	public void checkContinueShopping() throws Exception{

		popup.escPopup();
		
		waitForElement(btnContinueShopping);
		
		waitForElementInvisible(loadingWheel);
		
		click(btnContinueShopping);

	}


}
