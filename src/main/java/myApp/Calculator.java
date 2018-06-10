package myApp;

public class Calculator {

	public double add(double... nums ){

		int total=0;


		for (double num:nums){

			total=(int) (total+num);
		}

		return total;

	}
	
	
	public int subtract(int firstNum,int secondNum ){

		return firstNum - secondNum;

	}

}
