package com.anubhav.redis;

import com.anubhav.websockets.WebSocketSessionManager;
import io.lettuce.core.pubsub.RedisPubSubListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

public class SubscriberHelper implements RedisPubSubListener<String, String> {
    private WebSocketSessionManager webSocketSessionManager;

    private static final Logger logger = LoggerFactory.getLogger(SubscriberHelper.class);

    public SubscriberHelper(WebSocketSessionManager webSocketSessionManager){
        this.webSocketSessionManager = webSocketSessionManager;
    }
    @Override
    public void message(String channel, String message) {
        logger.info("got the message = "+ channel+ " and "+ message);
        this.webSocketSessionManager.getWebSocketSessionByChannelId(channel).forEach(x -> {
            try {
                logger.info("going to send the message to websocket {}", x.getId());
                x.sendMessage(new TextMessage("message = " + message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void message(String s, String k1, String s2) {

    }

    @Override
    public void subscribed(String s, long l) {

    }

    @Override
    public void psubscribed(String s, long l) {

    }

    @Override
    public void unsubscribed(String s, long l) {

    }

    @Override
    public void punsubscribed(String s, long l) {

    }
}
