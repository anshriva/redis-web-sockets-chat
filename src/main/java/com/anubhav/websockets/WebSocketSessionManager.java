package com.anubhav.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Component
public class WebSocketSessionManager {
    private final Map<String, WebSocketSession> webSocketSessionByUserId = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionManager.class);

    public void addWebSocketSession(WebSocketSession webSocketSession){
        String userId = WebSocketHelper.getUserIdFromSessionAttribute(webSocketSession);
        logger.info("got request to add session id {} for user id {} ", webSocketSession.getId(), userId);
        this.webSocketSessionByUserId.put(userId,webSocketSession);
        logger.info("added session id {} for user id {}", webSocketSession.getId(), userId);
    }

    public void removeWebSocketSession(WebSocketSession webSocketSession){
        String userId = WebSocketHelper.getUserIdFromSessionAttribute(webSocketSession);
        logger.info("got request to remove session id {} for user id {}", webSocketSession.getId(), userId);
        this.webSocketSessionByUserId.remove(userId);
        logger.info("removed session id {} for user id {}", webSocketSession.getId(), userId);
    }

    public WebSocketSession getWebSocketSessions(String userId){
        return this.webSocketSessionByUserId.get(userId);
    }
}
