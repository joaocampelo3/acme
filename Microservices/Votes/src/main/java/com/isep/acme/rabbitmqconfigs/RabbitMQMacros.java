package com.isep.acme.rabbitmqconfigs;

public class RabbitMQMacros {

    public final static String PREFIX = "fanout.";
    public final static String PUBLISH_QUEUE_NAME = PREFIX + "PublishVotesQueue";
    public final static String SUBSCRIBE_QUEUE_NAME = PREFIX + "SubscribeVotesQueue";
    public final static String PUBLISH_EXCHANGE_NAME = PREFIX + "PublishVotesExchange";
    public final static String SUBSCRIBE_EXCHANGE_NAME = PREFIX + "SubscribeVotesExchange";
    public final static String EXCHANGE_NAME = PREFIX + "ExchangeVotesQueue";
    public final static String VOTES_PUBLISH_KEY = "VotesPublishKey";
}
