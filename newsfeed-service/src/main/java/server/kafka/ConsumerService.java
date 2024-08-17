package server.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ConsumerService {
    @KafkaListener(topics = "post-event", groupId = "newsfeed-service-group")//확인 필요
    public void listenPostEvent(PostEventDto postEventDto) {
        // JSON 데이터를 AData 객체로 변환
        switch (postEventDto.getType()) {
            // feign client로 userId의 follower를 받음
            case "CREATED":
                // key == follow Id 인 redis에 postId를 넣음
                break;
            case "DELETED":
                // key == follow Id 인 redis에 postId를 지음
                break;
        }
    }
}
