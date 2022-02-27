Online Bakery
=============

- [Available APIs](#available-apis)
    - [Product](#product)
    - [Basket](#basket)
- [Preloaded Data](#preloaded-data)
- [Assumptions](#Assumptions)
- [Example Usage](#example-usage)
    - [Keyword Search](#keyword-search)
    - [Viewing a Product](#viewing-a-product)
    - [Adding a Product to Basket](#adding-a-product-to-basket)
    - [Updating an Existing Basket Item](#updating-an-existing-basket-item)
    - [Out of Stock](#out-of-stock)
    - [Checking the Basket](#checking-the-basket)
        - [In-Cart Basket Items](#in-cart-basket-items)
        - [Checked-Out Basket Items](#checked-out-basket-items)
        - [Bought Basket Items](#bought-basket-items)

# Available APIs
## Product

| Type | URL                                | Usage                                    |
|------|------------------------------------|------------------------------------------|
| GET  | /products                          | Get all products                         |
| GET  | /products/{productId}              | Get the product with the given id        |
| GET  | /products/search?keyword={keyword} | Search for products matching the keyword |


## Basket

| Type | URL                                 | Usage                                                    |
|------|-------------------------------------|----------------------------------------------------------|
| PUT  | /baskets                            | Add or update a basket item                              |
| GET  | /baskets/inCart?userId={userId}     | Get all basket items with status INCART for the user     |
| GET  | /baskets/checkedOut?userId={userId} | Get all basket items with status CHECKEDOUT for the user |
| GET  | /baskets/bought?userId={userId}     | Get all basket items with status BOUGHT for the user     |

# Preloaded Data

The store's database is preloaded with
- 3 users, whose ids are \[1, 2, 3\]
- 7 products
- 1 basket item

# Assumptions
Each user's browser knows the user's id and has it saved for later use.

The application is started with
```
assignment-java-boot-camp/bakery (main) $ ./mvnw spring-boot:run
```

# Example Usage
For improved readability, all cURL command in this section will use the silent option `-s`
and its output will be piped to `json_pp`. 

## Keyword Search
A user with id = 3 logs into the store and searches for keyword "washing machine".

The search products no result, because this is a bakery, not PowerMall.
We don't sell any washing machine here.
```
$ curl -s http://localhost:8080/products/search?keyword=washing%20machine | json_pp
{
   "message" : "No product found for keyword 'washing machine'"
}
```

The user performs a second search with keyword "panini".

The search returns 2 products whose names contain "panini".
```
$ curl -s http://localhost:8080/products/search?keyword=panini | json_pp
[
   {
      "amountInStock" : 5,
      "id" : 2,
      "name" : "chicken panini",
      "priceDiscount" : 0,
      "priceOriginal" : 100,
      "ratingAverage" : 0,
      "ratingCount" : 0,
      "seller" : "holey"
   },
   {
      "amountInStock" : 7,
      "id" : 3,
      "name" : "fattie pattie panini",
      "priceDiscount" : 0,
      "priceOriginal" : 100,
      "ratingAverage" : 0,
      "ratingCount" : 0,
      "seller" : "holey"
   }
]
```

## Viewing a Product
The user clicks the "chicken panini" thumbnail.
The click takes her to a page displaying information for this particular product.

The information comes from a GET request with the product id .
From the returned value of the previous keyword search,
the browser knows that "chicken panini" has product id 2.
```
$ curl -s http://localhost:8080/products/2 | json_pp
{
   "amountInStock" : 5,
   "id" : 2,
   "name" : "chicken panini",
   "priceDiscount" : 0,
   "priceOriginal" : 100,
   "ratingAverage" : 0,
   "ratingCount" : 0,
   "seller" : "holey"
}
```

## Adding a Product to Basket
The user wants to order 3 pieces of chicken panini.
To add these 3 pieces to her basket, the browser calls Basket PUT API.

The request returns JSON representation of this newly created Basket entity.
```
$ curl -s -X PUT localhost:8080/baskets -H 'Content-type:application/json' \
-d '{"userId": 3, "productId": 2, "amount": 3}' | json_pp
{
   "amount" : 3,
   "id" : 1,
   "product" : {
      "amountInStock" : 5,
      "id" : 2,
      "name" : "chicken panini",
      "priceDiscount" : 0,
      "priceOriginal" : 100,
      "ratingAverage" : 0,
      "ratingCount" : 0,
      "seller" : "holey"
   },
   "status" : "INCART",
   "user" : {
      "address" : "A dentist clinic",
      "id" : 3,
      "name" : "Hermione Granger",
      "username" : "user3"
   }
}
```

## Updating an Existing Basket Item
The user changes her mind about the amount of chicken panini she wants to order.
3 might be too much.

"Let's try just one piece", she thinks.
"I can always come back and order more if I like it."

So she updates her basket entry by calling the same PUT API,
but with an amount of 1.

The returned value shows that her basket entry has been updated.
```
$ curl -s -X PUT localhost:8080/baskets -H 'Content-type:application/json' \
-d '{"userId": 3, "productId": 2, "amount": 1}' | json_pp
{
   "amount" : 1,
   "id" : 1,
   "product" : {
      "amountInStock" : 5,
      "id" : 2,
      "name" : "chicken panini",
      "priceDiscount" : 0,
      "priceOriginal" : 100,
      "ratingAverage" : 0,
      "ratingCount" : 0,
      "seller" : "holey"
   },
   "status" : "INCART",
   "user" : {
      "address" : "A dentist clinic",
      "id" : 3,
      "name" : "Hermione Granger",
      "username" : "user3"
   }
}
```

## Out of Stock
While browsing other products in the online bakery,
the user orders 150 pieces of "fattie pattie panini" (product id 3) by accident.

The application refuses her request because there are only 7 pieces
of this panini in stock.
```
$ curl -s -X PUT localhost:8080/baskets -H 'Content-type:application/json' \
-d '{"userId": 3, "productId": 3, "amount": 150}' | json_pp
{
   "message" : "Product ID 3 has only 7 piece(s) left in stock."
}
```

## Checking the Basket
### In-Cart Basket Items
"In Cart" items are the products added to the basket but not yet checked out.

After the accidental order attempt with 150 pieces of fattie pattie panini,
the user wants to check for items she has in her basket.

The call returns 1 basket item.
It's the chicken panini with an updated amount of 1.
```
$ curl -s http://localhost:8080/baskets/inCart?userId=3 | json_pp
[
   {
      "amount" : 1,
      "id" : 1,
      "product" : {
         "amountInStock" : 5,
         "id" : 2,
         "name" : "chicken panini",
         "priceDiscount" : 0,
         "priceOriginal" : 100,
         "ratingAverage" : 0,
         "ratingCount" : 0,
         "seller" : "holey"
      },
      "status" : "INCART",
      "user" : {
         "address" : "A dentist clinic",
         "id" : 3,
         "name" : "Hermione Granger",
         "username" : "user3"
      }
   }
]
```

### Checked-Out Basket Items
"Checked Out" items are the basket items in the middle of checkout process but not yet purchased.

Because the user has not clicked the "Checkout" button
(which will begin the checking out process),
she expects no checked-out item for her account.

And the API call correctly returns no check-out item.
```
$ curl -s http://localhost:8080/baskets/checkedOut?userId=3 | json_pp
[]
```

### Bought Basket Items
"Bought" items are the basket items bought by a user
after the user completes the checkout process with successful payment.

Back to our example user, she browses all of her purchased items
by calling the Bought Basket GET API.

The returned value shows that she once bought 4 pieces of English muffin from the store.
```
$ curl -s http://localhost:8080/baskets/bought?userId=3 | json_pp
[
   {
      "amount" : 4,
      "id" : 1,
      "product" : {
         "amountInStock" : 8,
         "id" : 6,
         "name" : "english muffin",
         "priceDiscount" : 0,
         "priceOriginal" : 100,
         "ratingAverage" : 0,
         "ratingCount" : 0,
         "seller" : "pine street"
      },
      "status" : "BOUGHT",
      "user" : {
         "address" : "A dentist clinic",
         "id" : 3,
         "name" : "Hermione Granger",
         "username" : "user3"
      }
   }
]
```