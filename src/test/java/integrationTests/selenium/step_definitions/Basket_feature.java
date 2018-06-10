package integrationTests.selenium.step_definitions;

//JUNIT Test framework
import org.junit.Assert;

//Cucumber Test framework
import cucumber.api.java.en.*;

//POM Object
import integrationTests.selenium.page_object_model.Page_object_model;

public class Basket_feature extends Page_object_model {

	@When("^customer views basket$")
	public void customer_views_basket() throws Throwable {

		popup.escPopup(); 
		mainHeader.openBasket();
		
	}

	@When("^adds product to basket$")
	public void adds_product_to_basket() throws Throwable {

		productPage.adds_product_to_basket_via_productPage();

	}

	@When("^adds product: (.+) to basket twice$")
	public void adds_product_to_basket_twice(String product) throws Throwable {

		productPage.adds_product_to_basket_via_productPage();
		productPage.adds_product_to_basket_via_productPage();

	}

	@When("^adds first product to basket x(\\d+) quantity$")
	public void adds_first_product_to_basket_x_quantity(int quantity) throws Throwable {

		productResults.add_first_product_to_Basket_via_productResults(quantity);

	}

	@When("^adds second product to basket x(\\d+) quantity$")
	public void adds_second_product_to_basket_x_quantity(int quantity) throws Throwable {

		productResults.add_second_product_to_Basket_via_productResults(quantity);

	}
	
	@Then("^empty basket is shown$")
	public void empty_basket_is_shown() throws Throwable {

		popup.escPopup(); 
		
		Assert.assertTrue(textExists("Your trolley is currently empty") ||
				textExists("your shopping trolley is empty"));
		
	}
	
	@Then("^basket with (.+) products and (.+) quantity is shown$")
	public void basket_with_products_is_shown(String productCount, String quantityCount) throws Throwable {

		popup.escPopup();
		basket.wait_for_basket_to_load();

		//convert expected cucumber values from strings to integers
		int iproductCount = Integer.valueOf(productCount);
		int iquantityCount = Integer.valueOf(quantityCount);	

		Assert.assertEquals(iproductCount,basket.productCount());
		Assert.assertEquals(iquantityCount,basket.quantityCount());

	}
	
}
