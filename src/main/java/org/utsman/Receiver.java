package org.utsman;

public class Receiver {

    public static void main(String[] args) {
        RabbitConnection connection = new RabbitConnection.Builder()
                .setQueueName("hallo")
                .build("localhost");

        connection.receiver((queueName, consumerTag, message) -> {
            Utils.printLn(" [x] received: '" + message + "'");
        });
    }
}