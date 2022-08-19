# rabbitmq-products-app
Simple project with some RabbiteMQ examples


# Setup 

Start docker compose from main directory with command:

``docker compose up -d --build``

Call ``http://localhost:15672`` in your browser, then you can log in with 'guest' as login and password.

In RabbitMQ GUI you need to create new exchange with name ``x.products``

Then create three queues for our test cases ``q.books``, ``q.expensive-clothings`` and ``q.cheap-products``.

TODO - bindings