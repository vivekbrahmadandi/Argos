package integrationTests.selenium.page_object_model;

//JUNIT Test framework
import org.junit.Assert;

import org.openqa.selenium.By;

public class POM_mainHeader extends Page_object_model {

	public By btnBasket = By.xpath("(//span[@class=\"argos-header__link-label\"])[2]");
	public By txtBasketCount = By.xpath("//span[@class=\"argos-header__trolley-count argos-header__trolley-badge badge\"]");
	public By txtSearchBar = By.xpath("//*[@id=\"searchTerm\"]");
	public By btnSearch = By.xpath("//button[@class=\"argos-header__search-button\"]");
	public By btnLogin = By.xpath("//a[@class=\"font-condensed uppercase argos-header__link argos-header__link--signin\"]");

	public By txtCategory = By.xpath("//li[@class=\"font-condensed-extra-bold uppercase meganav__nav-item meganav__nav-item--categories \"]");

	//Category Links
	public By LinkTechnology = By.linkText("TECHNOLOGY"); 
	public By LinkHomeAndGarden = By.linkText("HOME & GARDEN");
	public By LinkBabyAndNursery = By.linkText("BABY & NURSERY");
	public By LinkToys = By.linkText("TOYS");
	public By LinkSportsAndLeisure = By.linkText("SPORTS & LEISURE");
	public By LinkHealthAndBeauty = By.linkText("HEALTH & BEAUTY");
	public By LinkClothing = By.linkText("CLOTHING");
	public By LinkJewelleryAndWatches = By.linkText("JEWELLERY & WATCHES");

	//Sub-Category links
	public By LinkTelevisionsAndAccessories = By.linkText("Televisions & Accessories"); 
	public By LinkLaptopsAndPCs = By.linkText("Laptops & PCs"); 
	public By LinkLivingRoomFurniture = By.linkText("Living Room Furniture"); 
	public By LinkBedding = By.linkText("Bedding"); 
	public By LinkTravel = By.linkText("Travel"); 
	public By LinkMaternity = By.linkText("Maternity"); 
	public By LinkWomenClothing = By.linkText("All Women's Clothing"); 
	public By LinkMenClothing = By.linkText("All Men's Clothing"); 

	//Niche-Category links
	public By LinkDashCams = By.linkText("Dash Cams"); 
	public By LinkSofas = By.linkText("Sofas"); 
	public By LinkPushchairs = By.linkText("Pushchairs"); 
	public By LinkBatteries = By.linkText("Batteries"); 
	public By LinkTreadmills = By.linkText("Treadmills"); 
	public By LinkHairDryers = By.linkText("Hair Dryers"); 
	public By LinkBras = By.linkText("Bras"); 
	public By LinkLadiesEarrings = By.linkText("Earrings"); 
	
	public By popupMenu = By.xpath("//span[@class=\"meganav__level-2-title\"]");	
	//<span class="meganav__level-2-title">Womens Clothing</span>
	
	
	public void clickCategory(String category) throws Exception{
		
		switch(category){
		case "TECHNOLOGY": {click(LinkTechnology); break;}
		case "HOME AND GARDEN": {click(LinkHomeAndGarden); break;}
		case "BABY AND NURSERY": {click(LinkBabyAndNursery); break;}
		case "TOYS": {click(LinkToys); break;}
		case "SPORTS AND LEISURE": {click(LinkSportsAndLeisure); break;}
		case "HEALTH AND BEAUTY": {click(LinkHealthAndBeauty); break;}
		case "CLOTHING": {click(LinkClothing); break;}
		case "JEWELLERY AND WATCHES": {click(LinkJewelleryAndWatches); break;}
		default: Assert.fail("Category not defined in Selenium page object model (POM) - contact tester");
		}
		
	}
	
	
	public void mouseToCategory(String category) throws Exception{
		

		switch(category){
		case "TECHNOLOGY": {mouseTo(LinkTechnology); break;}
		case "HOME AND GARDEN": {mouseTo(LinkHomeAndGarden); break;}
		case "BABY AND NURSERY": {mouseTo(LinkBabyAndNursery); break;}
		case "TOYS": {mouseTo(LinkToys); break;}
		case "SPORTS AND LEISURE": {mouseTo(LinkSportsAndLeisure); break;}
		case "HEALTH AND BEAUTY": {mouseTo(LinkHealthAndBeauty); break;}
		case "CLOTHING": {mouseTo(LinkClothing); break;}
		case "JEWELLERY AND WATCHES": {mouseTo(LinkJewelleryAndWatches); break;}
		default: Assert.fail("Category not defined in Selenium page object model (POM) - contact tester");
		}
	
		waitForElement(popupMenu);

	}
	
	public void clickSubCategory(String category) throws Exception{
		
		switch(category){
		case "televisions and accessories": {click(LinkTelevisionsAndAccessories); break;}
		case "Laptops and PCs": {click(LinkLaptopsAndPCs); break;}
		case "living room furniture": {click(LinkLivingRoomFurniture); break;}
		case "Bedding": {click(LinkBedding); break;}
		case "Travel": {click(LinkTravel); break;}
		case "Maternity": {click(LinkMaternity); break;}
		case "Womens": {click(LinkWomenClothing); break;}
		case "Mens": {click(LinkMenClothing); break;}
		default: Assert.fail("Category not defined in Selenium page object model (POM) - contact tester");
		}	

		
	}
	
	public void clickNicheCategory_viaMainheader(String nicheCategory) throws Exception{
		
		switch(nicheCategory){

		case "Dash Cams": {click(LinkDashCams); break;}
		case "Sofas": {click(LinkSofas); break;}
		case "Pushchairs": {click(LinkPushchairs); break;}
		case "Batteries And Rechargeable Batteries": {click(LinkBatteries); break;}
		case "Treadmills": {click(LinkTreadmills); break;}
		case "Hair Dryers": {click(LinkHairDryers); break;}
		case "Bras": {click(LinkBras); break;}
		case "Ladies' Earrings": {click(LinkLadiesEarrings); break;}
		default: Assert.fail("Category not defined in Selenium page object model (POM) - contact tester");
		}
	
	}
	
	public void searchBar(String category) throws Exception{
		
		popup.escPopup(); 
		
		sendkeys(txtSearchBar,category);
		click(btnSearch);
		
	}	
	
	public void openBasket() throws Exception{
		
		scrollTop();
		click(btnBasket);	
		
	}	
	
}
