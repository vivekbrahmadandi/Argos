package integrationTests.selenium.page_object_model;

import org.junit.Assert;
import org.openqa.selenium.By;


public class POM_categorySplashPage extends Page_object_model {

public By txtH1 = By.xpath("//h1");

//Niche-Category links
public By LinkHDMIandCables = By.linkText("HDMI cables and optical cables"); 
public By LinkTVremoteControls = By.linkText("TV remote controls"); 
public By LinkGaminglaptopsAndPCs = By.linkText("Gaming laptops and PCs"); 
public By LinkiMacs = By.linkText("iMacs"); 
public By LinkArmchairsAndChairs = By.linkText("Armchairs and chairs"); 
public By LinkCDandDVDstorage = By.linkText("CD and DVD storage"); 	
public By LinkDuvets = By.linkText("Duvets"); 
public By LinkPillows = By.linkText("Pillows"); 	
public By LinkPushchairs = By.linkText("Pushchairs"); 
public By LinkBabycarriers = By.linkText("Baby carriers"); 	
public By LinkMaternityAccessories = By.linkText("Maternity accessories"); 	
public By LinkAccessories = By.linkText("Accessories"); 
public By LinkDresses = By.linkText("Dresses"); 	
public By LinkCoatsAndJackets = By.linkText("Coats and jackets"); 	


public void clickNicheCategory_viaCategorySplashPage(String nicheCategory) throws Exception{
	
	switch(nicheCategory){
	case "hdmi-cables-and-optical-cables": {click(LinkHDMIandCables); break;}
	case "tv-remote-controls": {click(LinkTVremoteControls); break;}
	case "Gaming laptops and PCs": {click(LinkGaminglaptopsAndPCs); break;}
	case "imacs": {click(LinkiMacs); break;}
	case "armchairs-and-chairs": {click(LinkArmchairsAndChairs); break;}
	case "cd-and-dvd-storage": {click(LinkCDandDVDstorage); break;}
	case "duvets": {click(LinkDuvets); break;}
	case "pillows": {click(LinkPillows); break;}
	case "pushchairs": {click(LinkPushchairs); break;}
	case "baby-carriers": {click(LinkBabycarriers); break;}
	case "maternity-accessories": {click(LinkMaternityAccessories); break;}
	case "accessories": {click(LinkAccessories); break;}
	case "dresses": {click(LinkDresses); break;}
	case "coats-and-jackets": {click(LinkCoatsAndJackets); break;}

	default: Assert.fail("Category not defined in Selenium page object model (POM) - contact tester");
	}

}


}
