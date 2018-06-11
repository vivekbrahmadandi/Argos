package integrationTests.selenium.page_object_model;

import org.openqa.selenium.By;

public class POM_popup extends Page_object_model {

	private By btnClose = By.xpath("//a[@class=\"acsCloseButton acsAbandonButton \"]");	
	public By linkCookieGotIt = By.linkText("GOT IT");
	public By linkCookieGotIt_lowercase = By.xpath("//*[@id=\"__tealiumGDPRecModal\"]/div/div/div[2]/a");
	
	

	

	
	//*[@id="__tealiumGDPRecModal"]/div/div/div[2]/a
	
	
	public void escPopup() throws Exception{

		Thread.sleep(500);

		if(elementExists(btnClose)){ 

			click(btnClose);

		}
//		if(elementExists(linkCookieGotIt)){
//
//			//click(linkCookieGotIt);
//
//		}	
//		
		if(elementExists(linkCookieGotIt_lowercase)){

			click(linkCookieGotIt_lowercase);

		}	
		

	}

}
