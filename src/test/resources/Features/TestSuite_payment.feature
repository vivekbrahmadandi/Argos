Feature: Payment 
		 Customer has ability to pay with PayPal (API test). 


  @Payment 
  Scenario Outline: customer confirms payment with paypal
	When server creates payment order against paypal API
	And customer user id: <user id> confirms payment order 
	And server executes payment order against paypal API
	Then payment order is successfully processed 
	 Examples:
    | user id		|
    | 1.0			|

	
  @Payment
  Scenario: customer cancels payment with paypal
	When server creates payment order against paypal API
	And customer does not confirms payment order 
	And server executes payment order against paypal API
  	Then payment order is unsuccessfully processed 



    
