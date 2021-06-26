# RabbitMQ basic implementation on Java client

## Create connection
```java
RabbitConnection connection = new RabbitConnection.Builder()
    .setQueueName("hallo")
    .build("localhost");
```

## Receiver message
```java
connection.receiver((queueName, consumerTag, message) -> {
    // message received
    Utils.printLn(" [x] received: '" + message + "'");
});
```

## Sending message
```java
connection.send("message will be sent");
```

---