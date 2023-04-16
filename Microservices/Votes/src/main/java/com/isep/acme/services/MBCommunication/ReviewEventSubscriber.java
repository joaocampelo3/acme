package com.isep.acme.services.MBCommunication;

import com.isep.acme.events.ReviewEvent;
import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.Review;
import com.isep.acme.model.VoteTemp;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import com.isep.acme.repositories.VoteTempRepository;
import com.isep.acme.services.interfaces.VoteService;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
@EnableAsync
public class ReviewEventSubscriber {

    private static final String EXCHANGE_NAME = "review";

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteTempRepository voteTempRepository;

    @Autowired
    private RabbitMQHost rabbitMQHost;

    public void start() throws IOException, TimeoutException {

        // create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost.getHost());
        factory.setPort(Integer.parseInt(rabbitMQHost.getPort()));
        factory.setUsername(rabbitMQHost.getUsername());
        factory.setPassword(rabbitMQHost.getPassword());

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // create the exchange and queue
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        // bind the queue to the exchange for the review events
        channel.queueBind(queueName, EXCHANGE_NAME, "review.*");

        // create a consumer and start consuming messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                String eventType = envelope.getRoutingKey().substring(envelope.getRoutingKey().lastIndexOf(".") + 1);
                String originService = envelope.getRoutingKey().substring(0, envelope.getRoutingKey().indexOf("."));
                System.out.println("Received event '" + eventType + "' from service '" + originService + "' with message '" + message + "'");

                // parse the message as a ProductCreatedEvent or ReviewCreatedEvent
                if (eventType.equals("review_created") || eventType.equals("reviewFromVote_created") || eventType.equals("review_updated") || eventType.equals("review_deleted")) {
                    ReviewEvent event = ReviewEvent.fromJson(message);
                    try {
                        handleReviewEvent(eventType, originService, event);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);

        // keep the subscriber running
        System.out.println("Waiting for events...");
        while (true) {
        }
    }

    private void handleReviewEvent(String eventType, String originService, ReviewEvent event) throws Exception {
        // handle the review event
        if (eventType.equals("review_created")) {
            final Review r = new Review(event.getVoteTempID());
            voteService.createReview(r);
        } else if (eventType.equals("reviewFromVote_created")) {

            final Review r = new Review(event.getVoteTempID());
            voteService.createReview(r);

            final Optional<VoteTemp> voteTemp = voteTempRepository.findByID(event.getVoteTempID());

            final VoteDTO voteDTO = voteService.create(new VoteDTO(voteTemp.get().getVoteTempUuid().toString(), voteTemp.get().getUserID(), voteTemp.get().getVote()), event.getReviewUuid());

            if (voteDTO != null){
                voteService.deleteTempByVoteID(event.getVoteTempID());
            }

        } else if (eventType.equals("review_updated")) {
            // do something with the review updated event
        } else if (eventType.equals("review_deleted")) {
            voteService.DeleteReview(event.getReviewUuid());
        }
    }

    @Bean
    @Async
    public void mainReviewSubscription() throws IOException, TimeoutException {
        this.start();
    }

}
