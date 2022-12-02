package com.anubhav.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WebSocketSessionManager {
    private final Map<String, Set<WebSocketSession>> webSocketSessions = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionManager.class);

    public void addWebSocketSession(WebSocketSession webSocketSession){
        String channelId = WebSocketHelper.getChannelIdFromSessionAttribute(webSocketSession);
        logger.info("got request to add session id {} to channel id {}", webSocketSession.getId(), channelId);
        if(!this.webSocketSessions.containsKey(channelId)){
            this.webSocketSessions.put(channelId,new HashSet<>());
        }
        this.webSocketSessions.get(channelId).add(webSocketSession);
        logger.info("added session id {} to channel id {}", webSocketSession.getId(), channelId);
    }


    public void removeWebSocketSession(WebSocketSession webSocketSession){
        String channelId = WebSocketHelper.getChannelIdFromSessionAttribute(webSocketSession);
        logger.info("got request to remove session id {} from channel id {}", webSocketSession.getId(), channelId);
        if(this.webSocketSessions.containsKey(channelId)) {
            var sessionsInChannel = this.webSocketSessions.get(channelId);
            var matchingSessionToBeRemoved = sessionsInChannel.stream().
                    filter(x -> x.getId().equalsIgnoreCase(webSocketSession.getId())).
                    collect(Collectors.toSet());
            sessionsInChannel.removeAll(matchingSessionToBeRemoved);
        }
        logger.info("removed session id {} from channel id {}", webSocketSession.getId(), channelId);
    }

    public Set<WebSocketSession> getWebSocketSessionByChannelId(String channelId){

        if(!this.webSocketSessions.containsKey(channelId)){
            return new HashSet<>();
        }
         return this.webSocketSessions.get(channelId);
    }
}
