package com.isep.acme.services.impl;

import com.isep.acme.model.VoteTemp;
import com.isep.acme.services.interfaces.VoteSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class VotesSenderServiceImpl implements VoteSenderService {

    private static final Logger LOGGER = Logger.getLogger(VotesSenderServiceImpl.class.getName());
    @Autowired
    private RabbitTemplate template;
    private final Queue queue = new Queue("votes");

/*    public static ConnectionFactory factory = new ConnectionFactory();

    @Bean
    public void rabbitMQConnection() {
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queue.getName(), false, false, false, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    @Override
    public void send() {
        VoteTemp votesTemp = new VoteTemp(Long.valueOf("1"), Long.valueOf("1"), "vote test",
                Long.valueOf("1"), "review test");
        String message = votesTemp.toDto().toJson();
        LOGGER.log(Level.INFO, " SENDING MESSAGE: '" + votesTemp.toDto().toJson() + "'");
        this.template.convertAndSend("my_exchange", "myEvent", message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
