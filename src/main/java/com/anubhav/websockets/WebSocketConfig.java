package com.anubhav.websockets;

import com.anubhav.redis.Publisher;
import com.anubhav.redis.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    WebSocketSessionManager webSocketSessionManager;

    @Autowired
    Publisher redisPublisher;

    @Autowired
    Subscriber redisSubscriber;

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketTextHandler(this.webSocketSessionManager, this.redisPublisher, this.redisSubscriber), "/user/*").
                addInterceptors(getParametersInterceptors()).
                setAllowedOriginPatterns("*");
    }

    @Bean
    public HandshakeInterceptor getParametersInterceptors() {
        return new HandshakeInterceptor() {
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) {

                String path = request.getURI().getPath();
                String userId = WebSocketHelper.getUserIdFromUrl(path);
                attributes.put(WebSocketHelper.userIdKey, userId);
                return true;
            }

            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Exception exception) {
                // Nothing to do after handshake
            }
        };
    }
}