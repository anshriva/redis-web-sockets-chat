package com.anubhav.websockets;

import com.anubhav.redis.Publisher;
import com.anubhav.redis.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

@Component
public class SocketTextHandler extends TextWebSocketHandler {


    private WebSocketSessionManager webSocketSessionManager;

    private Publisher redisPublisher;

    private Subscriber redisSubscriber;

    private static final Logger logger = LoggerFactory.getLogger(SocketTextHandler.class);

    public SocketTextHandler(WebSocketSessionManager webSocketSessionManager, Publisher redisPublisher, Subscriber redisSubscriber) {
        this.webSocketSessionManager = webSocketSessionManager;
        this.redisPublisher = redisPublisher;
        this.redisSubscriber = redisSubscriber;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.webSocketSessionManager.addWebSocketSession(session);
        String userId = WebSocketHelper.getUserIdFromSessionAttribute(session);
        this.redisSubscriber.subscribe(userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.webSocketSessionManager.removeWebSocketSession(session);
        String userId = WebSocketHelper.getUserIdFromSessionAttribute(session);
        this.redisSubscriber.unsubscribe(userId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {
        String payload = message.getPayload();
        String[] payLoadSplit = payload.split("->");
        String targetUserId  = payLoadSplit[0].trim();
        String messageToBeSent = payLoadSplit[1].trim();
        String userId = WebSocketHelper.getUserIdFromSessionAttribute(session);
        logger.info("got the payload {} and going to send to channel {}", payload, targetUserId);
        this.redisPublisher.publish(targetUserId, userId + ":" + messageToBeSent);
    }
}