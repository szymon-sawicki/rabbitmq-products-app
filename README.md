# rabbitmq-products-app

Simple project with some RabbiteMQ examples


# Setup 

Start docker compose from main directory with command:

``docker compose up -d --build``

Call ``http://localhost:15672`` in your browser, then you can log in with 'guest' as login and password.

For the test purposes create queues: 

``q.books``, ``q.clothing``, ``q.electronics``, ``q.expensive-clothings``, ``q.cheap-products``, ``q.warehouse``, 
``q.archive``

# Exchange of type DIRECT 

In this type of exchange incoming products are sent to proper queues on the basis of category

In RabbitMQ GUI you need to create new exchange with name ``x.products-direct`` then in section bindings:

``q.books`` -  	``BOOKS`` 

``q.clothing`` - ``CLOTHING``

``q.electronics`` - ``ELECTRONICS``

# Exchange of type TOPIC 

In this type of exchange incoming products are sent to proper queues on the basis of routingKey which is dynamically generated in ProductProducer.

In RabbitMQ GUI you need to create new exchange with name ``x.products-topic`` then in section bindings:

``q.books`` -  	``BOOKS.#`` for all products of books category

``q.cheap-products`` - ``#.low`` for all products with price lower than 10 

``q.expensive-clothings`` - ``CLOTHING.high`` for all clothing products with price greater than 100

# Exchange of type FANOUT

In this case all messages are sent to all bounded queues. 

In RabbitMQ GUI you need to create new exchange with name ``x.products-fanout`` and type fanout then in section bindings:

