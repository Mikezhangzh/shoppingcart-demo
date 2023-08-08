# shoppingcart-demo
### Summary
#### Requirments
 _Admin User Operations_
- _Create a new product_
- _Remove a product_
- _Add discount deals for products (Example: Buy 1 get 50% off the second)_

_Customer Operations_
- _Add and remove products to and from a basket_
- _Calculate a receipt of items, including all purchases, deals applied and total price_

#### Tech stack
- Springboot Rest API
- Springboot validation
- Springboot JPA
- Springdoc - swagger plugin
- H2 in memory database
- Maven
- Junit

### Implementation Notes
The data model design was intentionally kept simple but was sufficient for the above requirements.

#### Shopping cart
The shopping cart concept here is logical. It represents the collection of product items (CartItem) customer 
saved in the database. There will be no ShoppingCart entity or shopping cart table. CartItem (product + quantity)
is the Entity class that customer can save, update and delete. When retrieving shopping cart detail for a customer,
the api will query all the CartItem objects in the database for the customer. In that sense, each customer 
can have only one active "shopping cart". When the customer has no saved CartItem, the customer's shopping cart is
empty - which can happen when the customer is a new customer or the customer checked out the shopping cart. 

#### Product discount
This demo only considers discount related to products and will be applied only to the CartItem (product + quantity) the 
customer saved (in the "shopping cart"). It will not consider other types promotion such as "buy $100+ get $15 off"
(value based), or "Prime member get free shipping" (membership based).

The following is the ProductDiscount modol design:

| ProductDiscount |                                                                                       |
|-----------------|---------------------------------------------------------------------------------------|
| product_id      | reference the product that this discount related to                                   |
| discount_value  | discount value can be in percentage or absolute Dollar amount                         |
| discount_unit   | percentage or currency                                                                |
| order_quantity  | the quantity of the product in cart                                                   |
| apply_quantity  | the number of products the discount will be applied to                                |
| discount_type   | shared or exclusive. If exclusive the discount cannot be applied with other discount. |

Example:
- buy one get one free discount:
```
  {
    "product_id": 1,
    "discount_value": 100,
    "discount_unit": "percentage",
    "order_quantity": 2,
    "apply_quantity": 1,
    "discount_type": "shared"
  } 
  ```
- straight 15% off discount:
```
  {
    "product_id": 1,
    "discount_value": 15,
    "discount_unit": "percentage",
    "order_quantity": 1,
    "apply_quantity": 1,
    "discount_type": "shared"
  } 
  ```
### Running and testing
- Checkout the source code from 
```https://github.com/Mikezhangzh/shoppingcart-demo.git```
- Built by maven. From the root directory of the project, run 
```$ mvn clean install```
- To run demo application on an enbedded server on localhost, run
```$ mvn spring-boot:run```
- View the H2 in-memory database, user: sa; password: password, go to
```http://localhost:8080/h2-console```
- view the demo API's OpenAPI specification on enbedded Swagger UI, go to
```http://localhost:8080/swagger-ui/index.html```
- The above Swagger UI is configured to allow issuing API test requests, so this is also a convenient place to test the API.