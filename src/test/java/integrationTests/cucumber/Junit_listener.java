package integrationTests.cucumber;

import java.awt.Desktop;
import java.io.File;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import integrationTests.selenium.Selenium_core;

public class Junit_listener extends RunListener {

	@Override
	public void testRunStarted(Description description) throws Exception {

	}

	@Override
	public void testRunFinished(Result result) throws Exception {

		String testReport = System.getProperty("user.dir") + "\\target\\Masterthought\\feature-overview.html";	

		System.out.println("---------------------------------------------");
		System.out.println("[Test Report Location] " + testReport);
		System.out.println("---------------------------------------------");
		
		Cucumber_report_generate.GenerateMasterthoughtReport();
		
		Selenium_core.quitWebDriver();
		
		//Auto open report
		//File htmlFile = new File(testReport);
		//Desktop.getDesktop().browse(htmlFile.toURI());

	}

	@Override
	public void testStarted(Description description) throws Exception {

	}

	
	@Override
	public void testFinished(Description description) throws Exception {
	
		
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
	
	}
	

}