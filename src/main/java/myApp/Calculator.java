package myApp;

public class Calculator {

	public int add(int... nums ){

		int total=0;


		for (int num:nums){

			total=total+num;
		}

		return total;

	}
	
	
	public int subtract(int firstNum,int secondNum ){

		return firstNum - secondNum;

	}

}
