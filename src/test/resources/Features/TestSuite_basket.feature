Feature: Basket 
		 Customer has ability to Add, view, update and delete items from basket.

  @Basket @Short
  Scenario: Customer views empty basket
	Given customer is anywhere on website
	When customer views basket
	Then empty basket is shown

 
  @Basket   
  Scenario Outline: Customer views basket with one item x1 quantity
	Given customer is on product page : <Product>
	And adds product to basket
	And customer views basket
	Then basket with <ProductCount> products and <Quantity> quantity is shown
   Examples:
    | Product 					| ProductCount	| Quantity	|
    | XBOX		 				| 1				| 1			|
#    | COOKERS 					| 1				| 1			|
#    | PUSHCHAIR 				| 1				| 1			|
#    | SPIDERMAN					| 1				| 1			|
#    | FOOTBALL 					| 1				| 1			|
#    | WEIGHTS					| 1				| 1			|
#    | JEANS 					| 1				| 1			|       
#    | RING					 	| 1				| 1			|	


  @Basket @Retest
  Scenario Outline:  Customer views basket with one item x2 quantity
	Given customer is on product page : <Product>
	And adds product: <Product> to basket twice
	When customer views basket
	Then basket with <ProductCount> products and <Quantity> quantity is shown
   Examples:
    | Product 					| ProductCount	| Quantity	|
    | FOOTBALL 					| 1				| 2			|
    | WEIGHTS					| 1				| 2			|
    | JEANS 					| 1				| 2			|       

    
  @Basket 
  Scenario Outline: Customer views basket with two different items x1 quantity each
	Given customer is anywhere on website
    When customer searches by product: <Product> using search feature
	And adds first product to basket x1 quantity
	And adds second product to basket x1 quantity
	When customer views basket
	Then basket with <ProductCount> products and <Quantity> quantity is shown
   Examples:
    | Product 					| ProductCount	| Quantity	|
    | PUSHCHAIR 				| 2				| 2			|
    | SPIDERMAN					| 2				| 2			|
    | FOOTBALL 					| 2				| 2			|

    
  @Basket 
  Scenario Outline: Customer views basket with two different items x5 quantity and x7 quantity
	Given customer is anywhere on website
    When customer searches by product: <Product> using search feature
	And adds first product to basket x2 quantity
	And adds second product to basket x3 quantity
	When customer views basket
	Then basket with <ProductCount> products and <Quantity> quantity is shown
   Examples:
    | Product 					| ProductCount	| Quantity	|
    | XBOX		 				| 2				| 5			|
    | COOKERS 					| 2				| 5			|
    | JEANS 					| 2				| 5			|       






	