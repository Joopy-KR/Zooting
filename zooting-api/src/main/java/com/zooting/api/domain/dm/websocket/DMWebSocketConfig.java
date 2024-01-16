package com.zooting.api.domain.dm.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP를 사용할 수 있게 해주는 어노테이션
@CrossOrigin("*")
public class DMWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry endpointRegistry) { // 소켓 연결을 위해 사용할 엔드포인트 등록과 cors 오류 방지를 위해 허용할 Origin을 등록해둔다.
        endpointRegistry.addEndpoint("/ws")
//                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*")// 연결될 엔드포인트
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry brokerRegistry) { //구독과 발행 시 사용할 prefix를 정해준다.
        brokerRegistry.enableSimpleBroker("/sub");
        brokerRegistry.setApplicationDestinationPrefixes("/pub");
    }
}
