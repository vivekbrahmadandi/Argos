package integrationTests.selenium.page_object_model;

import org.openqa.selenium.By;

public class POM_popup extends Page_object_model {

	private By btnClose = By.xpath("//a[@class=\"acsCloseButton acsAbandonButton \"]");	

	public By linkCookieGotIt = By.xpath("//*[@id=\"__tealiumGDPRecModal\"]/div/div/div[2]/a");
	
	
	public void escPopup() throws Exception{

		Thread.sleep(500);

		if(elementExists(btnClose)){ 

			click(btnClose);
			waitForElementInvisible(btnClose);
		}

		if(elementExists(linkCookieGotIt)){

			click(linkCookieGotIt);
			waitForElementInvisible(linkCookieGotIt);

		}	
		
	}

}
