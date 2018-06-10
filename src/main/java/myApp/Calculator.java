package myApp;

public class Calculator {

	public long add(long... nums ){

		long total=0;


		for (long num:nums){

			total=total+num;
		}

		return total;

	}
	
	
	public int subtract(int firstNum,int secondNum ){

		return firstNum - secondNum;

	}

}
