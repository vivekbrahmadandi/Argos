package integrationTests.selenium.page_object_model;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.util.*;




public class POM_basket extends Page_object_model {

	public By txtH1 = By.xpath("//*[@id=\"emptytrolleylister\"]/div[1]/h1");


	public By dropQuantity = By.xpath("//select[starts-with(@id,'quantity')]");


	public By txtPrice = By.xpath("//span[@class=\"price\"]");
	public By btnRemove = By.xpath("//input[@class=\"input-link\"]");
	public By btnRemove2 = By.xpath("//button[starts-with(@class,'ProductCard__removeButton')]");
	
	
	public int productCount() throws InterruptedException{
	
		List<WebElement> rows = webdriver.findElements(dropQuantity);

		int productCount = elementCount(dropQuantity);
		//System.out.println("number of different products: " + productCount);
		return  productCount;
		
		
	}

	public int quantityCount() throws InterruptedException{

		int quantityCount = 0;
		
		List<WebElement> rows = webdriver.findElements(dropQuantity);

		Iterator<WebElement> iter = rows.iterator();
		while (iter.hasNext()) {

			WebElement element = iter.next();
			
			Select select = new Select(element);
			quantityCount = quantityCount + Integer.valueOf(select.getFirstSelectedOption().getText().replaceAll("\\s+",""));
			
			//System.out.println("quantity of product: " + quantityCount);
		}
		
		return quantityCount;

	}
	
	
	public void wait_for_basket_to_load() throws Exception{
	
		waitForElement(dropQuantity);
		
	}
	
}
