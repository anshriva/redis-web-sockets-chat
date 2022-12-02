package com.anubhav.websockets;

import com.anubhav.redis.Publisher;
import com.anubhav.redis.Subscriber;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class SocketTextHandler extends TextWebSocketHandler {


    private WebSocketSessionManager webSocketSessionManager;

    private Publisher redisPublisher;

    private Subscriber redisSubscriber;


    public SocketTextHandler(WebSocketSessionManager webSocketSessionManager, Publisher redisPublisher, Subscriber redisSubscriber) {
        this.webSocketSessionManager = webSocketSessionManager;
        this.redisPublisher = redisPublisher;
        this.redisSubscriber = redisSubscriber;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.webSocketSessionManager.addWebSocketSession(session);
        String channelId = WebSocketHelper.getChannelIdFromSessionAttribute(session);
        this.redisSubscriber.subscribe(channelId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.webSocketSessionManager.removeWebSocketSession(session);
        String channelId = WebSocketHelper.getChannelIdFromSessionAttribute(session);
        this.redisSubscriber.unsubscribe(channelId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {
        String payload = message.getPayload();
        String channelId = WebSocketHelper.getChannelIdFromSessionAttribute(session);
        this.redisPublisher.publish(channelId, payload);
    }
}