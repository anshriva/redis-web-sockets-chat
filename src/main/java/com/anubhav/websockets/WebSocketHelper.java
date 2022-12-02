package com.anubhav.websockets;

import org.springframework.web.socket.WebSocketSession;

public class WebSocketHelper {
    public static String channelIdKey = "channelId";


    public static String getChannelIdFromSessionAttribute(WebSocketSession webSocketSession) {
        return (String) webSocketSession.getAttributes().get(channelIdKey);
    }

    public static String getChannelIdFromUrl(String path){
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
