package org.utsman;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitConnection {

    private ConnectionFactory factory;
    private Channel channel;
    private String queueName;

    public ConnectionFactory getFactory() {
        return factory;
    }

    public Channel getChannel() {
        return channel;
    }

    private void setFactory(ConnectionFactory factory) {
        this.factory = factory;
    }

    private void setChannel(Channel channel) {
        this.channel = channel;
    }

    private void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void send(String message) {
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        try {
            channel.basicPublish("", queueName, null, body);
            Utils.printLn(" [v] sending message ..");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiver(Listener listener) {
        Utils.printLn(" [-] Waiting message..");
        DeliverCallback deliverCallback = ((consumerTag, message) -> {
           String msg = new String(message.getBody(), StandardCharsets.UTF_8);
           listener.onReceived(queueName, consumerTag, msg);
        });

        try {
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Builder {
        private String queueName;

        public Builder setQueueName(String queueName) {
            this.queueName = queueName;
            return this;
        }

        public RabbitConnection build(String host) {
            RabbitConnection rConnection = new RabbitConnection();
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(queueName, false, false, false, null);

                rConnection.setQueueName(queueName);
                rConnection.setFactory(factory);
                rConnection.setChannel(channel);

            } catch (IOException | TimeoutException e) {
                Utils.printLn(e.getMessage());
                e.printStackTrace();
            }

            return rConnection;
        }
    }

    interface Listener {
        void onReceived(String queueName, String consumerTag, String message);
    }
}
