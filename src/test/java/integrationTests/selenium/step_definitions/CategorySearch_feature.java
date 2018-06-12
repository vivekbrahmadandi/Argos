package integrationTests.selenium.step_definitions;

//JUNIT Test framework
import org.junit.Assert;

//Cucumber Test framework
import cucumber.api.java.en.*;

//POM Object
import integrationTests.selenium.page_object_model.Page_object_model;

public class CategorySearch_feature extends Page_object_model {

	String category;
	String nicheCategory;
	String unrecognisedCategory;
	
	@Given("^customer is anywhere on website$")
	public void customer_is_anywhere_on_website() throws Throwable {

		
		gotoHomePage();
		deleteCookies();
		popup.escPopup(); 
		
	}
	
	@When("^customer clicks on category: (.+) in the menu$")
	public void customer_clicks_on_a_category_in_the_menu(String category) throws Throwable {

		popup.escPopup();
		mainHeader.clickCategory(category);
		
		this.category = category;

	}

	@When("^customer hovers on category: (.+) in the menu$")
	public void customer_hovers_on_a_category_in_the_menu(String category) throws Throwable {
		
		popup.escPopup();
		mainHeader.mouseToCategory(category);
		
		this.category = category;
		
	}

	@When("^clicks on sub-category: (.+)$")
	public void clicks_on_a_sub_category(String category) throws Throwable {

		popup.escPopup(); 
		mainHeader.clickSubCategory(category);
		
		this.category = category;
		
	}


	@When("^clicks on niche-category via main header: (.+)$")
	public void clicks_on_a_niche_category_mh(String nicheCategory) throws Throwable {

		popup.escPopup(); 
		mainHeader.clickNicheCategory_viaMainheader(nicheCategory);
		
		//this.nicheCategory = nicheCategory;
		
	}

	
	@When("^clicks on niche-category via category splash screen: (.+)$")
	public void clicks_on_a_niche_category_css(String nicheCategory) throws Throwable {

		popup.escPopup(); 
		categorySplashPage.clickNicheCategory_viaCategorySplashPage(nicheCategory);
		
		//this.nicheCategory = nicheCategory;
		
	}
	
	@When("^customer searches and misspells (.+) with (.+) using search feature$")
	public void customer_searches_and_misspells_category_using_search_feature(String category, String misspelling) throws Throwable {
		
		popup.escPopup(); 
		mainHeader.searchBar(misspelling);

		this.category = category;
		
	}

	@When("^customer searches for unrecognised category: (.+) using search feature$")
	public void customer_searches_for_unrecognised_category_using_search_feature(String unrecognisedCategory) throws Throwable {
		
		popup.escPopup(); 
		
		mainHeader.searchBar(unrecognisedCategory);
		
		//this.unrecognisedCategory = unrecognisedCategory;
	}

	
	@Then("^category splash screen is shown$")
	public void category_splash_screen_is_shown() throws Throwable {

		popup.escPopup();
		Assert.assertEquals(category.toLowerCase(),getText(categorySplashPage.txtH1).toLowerCase());
		
	}
	
	@Then("^results of matching products are shown by order of relevance$")
	public void results_of_matching_products_are_shown_by_order_of_relevance() throws Throwable {

		popup.escPopup(); //if required
		Assert.assertEquals("Relevance",getDropDownMenuText(productResults.dropProductSort));

	}
	
	@Then("^results of matching products are shown by order of popularity$")
	public void results_of_matching_products_are_shown_by_order_of_popularity() throws Throwable {

		popup.escPopup(); 
		Assert.assertEquals("Most Popular",getDropDownMenuText(productResults.dropProductSort));

	}
	
	@Then("^no search results page is shown$")
	public void no_search_results_page_is_shown() throws Throwable {

		Assert.assertTrue(getText(productResults.txtNoResults).contains("couldn't find any products"));
			
	}	

}
