package org.utsman;

public class Sender {

    public static void main(String[] args) {
        RabbitConnection connection = new RabbitConnection.Builder()
                .setQueueName("hallo")
                .build("localhost");

        connection.send("oke sip hah");
    }
}
