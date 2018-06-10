//package integrationTests.selenium.step_definitions;
//
////JUNIT Test framework
//import org.junit.Assert;
//
////Cucumber Test framework
//import cucumber.api.java.en.*;
//
//import integrationTests.selenium.page_object_model.Page_object_model;
//import integrationTests.selenium.Read_xls_dataFiles;
//
//import java.util.List;
//
//public class Payment_feature extends Page_object_model {
//
//	String SOAPprojectDir = "C:\\Users\\workpeter\\Documents\\Eclipse-workspace\\ARGOS\\src\\test\\resources\\SOAPUI_projects\\";
//
//	String accessToken;
//	String confirmURL;
//	String executeURL_API;
//	
//	Boolean passed = false;
//	
//
//	@When("^server creates payment order against paypal API$")
//	public void server_creates_payment_order_against_paypal_API() throws Throwable {
//	
//		loadAPI(SOAPprojectDir + "PAYPAL_API.xml");
//		
//		List<String> logs = runAPI("PAYPAL_PAYMENT_TESTCASE","setPayment");
//	
//		showRunAPIlog(logs);
//
//		//Grab strings from SOAPUI log
//		accessToken = logs.get(0); 		
//		confirmURL = logs.get(2); 		
//		executeURL_API = logs.get(3); 	
//		
//	}
//
//	
//	@When("^customer user id: (.+) confirms payment order$")
//	public void customer_confirms_payment_order(String userID) throws Throwable {
//
//		restartWebDriver();
//		
//		Read_xls_dataFiles data = new Read_xls_dataFiles("C:\\Users\\workpeter\\Documents\\Eclipse-workspace\\ARGOS\\src\\test\\resources\\Data\\paypal_users.xls", "logins");
//
//		int userIDrow =data.findDataRow(userID);
//		
//		String username  = data.readData(userIDrow, 2);
//		String password  = data.readData(userIDrow, 3);	
//		
//		paypal.login(confirmURL,username,password);
//		paypal.confirmPayment();
//		
//		//Assert.fail();
//		
//	}
//
//	@When("^server executes payment order against paypal API$")
//	public void server_executes_payment_order_against_paypal_API()  {
//
//		//intentionally not asserting here, instead saving output to be asseted in the @Then steps
//		try{
//			
//			//re-running runAPI losses parameters between TestCases, so they need feeding back in.
//			setAPIproperties("access_token="+accessToken,"execute_url="+executeURL_API);
//			
//			runAPI("PAYPAL_PAYMENT_TESTCASE","executePayment");
//			passed = true;
//		}catch(AssertionError e){
//			passed = false;
//		}
//		
//	}
//
//	@When("^customer does not confirms payment order$")
//	public void customer_cancels_payment_order() throws Throwable {
//		
//		//Do nothing
//		
//	}
//	
//	
//	@Then("^payment order is successfully processed$")
//	public void payment_order_is_successfully_processed() throws Throwable {
//		
//		Assert.assertTrue(passed);
//		
//	}
//
//	@Then("^payment order is unsuccessfully processed$")
//	public void payment_order_is_unsuccessfully_processed() throws Throwable {
//		
//		Assert.assertTrue(!passed);
//		
//		
//	}
//
//
//
//}
