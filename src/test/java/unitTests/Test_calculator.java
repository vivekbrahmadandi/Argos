package unitTests;

import org.junit.Assert;
import org.junit.Test;

import cucumber.api.java.en.When;

import myApp.Calculator;

public class Test_calculator {

	Calculator Calculator = new Calculator();
	
	@Test
	public void test_add()  {

		System.out.println("unit Testing Calculator.add");
		
		Assert.assertEquals(6, Calculator.add(2,2,2));
		Assert.assertEquals(5, Calculator.add(5));
		Assert.assertEquals(60, Calculator.add(51,9));
	}
	
	@Test
	public void test_subtract()  {
		
		System.out.println("unit Testing Calculator.subtract");
		
		Assert.assertEquals(0, Calculator.subtract(2,2));
		Assert.assertEquals(-3, Calculator.subtract(5,8));
		Assert.assertEquals(40, Calculator.subtract(50,10));
	}
	
	
}
