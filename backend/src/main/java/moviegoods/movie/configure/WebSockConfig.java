package moviegoods.movie.configure;

import moviegoods.movie.domain.interceptor.HttpHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/sub"); //메세지 구독 요청
        config.setApplicationDestinationPrefixes("/pub");  //메세지 발행 요청


    }
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws-stomp").addInterceptors()
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS().setInterceptors(new HttpHandshakeInterceptor());

    }

}