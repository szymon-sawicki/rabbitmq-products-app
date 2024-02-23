# rabbitmq-products-app

Simple project with some RabbiteMQ examples


# Setup 

Start docker compose from main directory with command:

``docker compose up -d --build``

Call ``http://localhost:15672`` in your browser, then you can log in with 'guest' as login and password.

For the test purposes create queues: 

``q.books``, ``q.clothing``, ``q.electronics``, , ``q.books-clothings``, ``q.expensive-clothings``, ``q.cheap-products``, ``q.warehouse``, 
``q.archive``

In the main directory you can find postman collection with ready request for tests of each exchange type.

# Exchange of type DIRECT 

In this type of exchange incoming products are sent to proper queues on the basis of category

In RabbitMQ GUI you need to create new exchange with name ``x.products-direct`` then in section bindings:

``q.books`` - ``BOOKS`` 

``q.clothing`` - ``CLOTHING``

``q.electronics`` - ``ELECTRONICS``

# Exchange of type TOPIC 

In this type of exchange incoming products are sent to proper queues on the basis of routingKey which is dynamically generated in ProductProducer.

In RabbitMQ GUI you need to create new exchange with name ``x.products-topic`` then in section bindings:

``q.books`` - ``BOOKS.#`` for all products of books category

``q.cheap-products`` - ``#.low`` for all products with price lower than 100 

``q.expensive-clothings`` - ``CLOTHINGS.high`` for all clothing products with price greater than 200

# Exchange of type FANOUT

In this case all messages are sent to all bounded queues. 

In RabbitMQ GUI you need to create new exchange with name ``x.products-fanout``, you need to bound two queues ``q.warehouse`` and ``q.archive``

# Exchange of type HEADER

This type of exchange (like in direct) will send messages to proper category queues on the basis of the arguments declared and sent in requests header.

In RabbitMQ GUI you need to create new exchange with name ``x.products-header`` and type headers then 2 bindings with following bindings:

![](C:\development\workspaces\rabbitmq-products-app\doc\rabbitmq_header_exchange_bindings1.PNG)

![](C:\development\workspaces\rabbitmq-products-app\doc\rabbitmq_header_exchange_bindings2.PNG)