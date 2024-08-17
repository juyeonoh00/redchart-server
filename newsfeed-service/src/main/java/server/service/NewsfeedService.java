package server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsfeedService {

    private KafkaTemplate<String, String> kafkaTemplate;

    private RedisTemplate<String, String> redisTemplate;


}