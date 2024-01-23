package com.zooting.api.domain.dm.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP를 사용할 수 있게 해주는 어노테이션
public class DMWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 소켓 연결을 위해 사용할 엔드포인트 등록과 cors 오류 방지를 위해 허용할 Origin을 등록해둔다.
     * 엔드포인트: /ws/dm
     * SockJS 사용
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry endpointRegistry) {
        endpointRegistry.addEndpoint("/ws/dm")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     *  /sub 엔드포인트를 활용하여 구독 ex) api/sub/dm/{receiver}
     *  /pub 엔드포인트를 활용하여 전송 ex) api/pub/dm/message
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry brokerRegistry) {
        brokerRegistry.enableSimpleBroker("api/sub");
        brokerRegistry.setApplicationDestinationPrefixes("api/pub");
    }
}
