package server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import server.feign.server.FollowersListDto;
import server.feign.server.UserController;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {
    private final UserController userController;
//    private final RedisTemplate<String, ArrayList<String>> redisTemplate;
    private final RedisTemplate<String, ArrayList<String>> redisTemplate;

//    @Autowired
//    public ConsumerService(ServerController serverController, RedisTemplate<String, String> redisTemplate) {
//        this.serverController = serverController;
//        this.redisTemplate = redisTemplate;
//    }

    @KafkaListener(topics = "post-event", groupId = "newsfeed-service-group")//확인 필요
    public void listenPostEvent(PostEventDto postEventDto) {
        FollowersListDto followersList= userController.getFollowersById(postEventDto.getPostWriterId());
        String postId = postEventDto.getPostId().toString();
        ArrayList<String> mycurrentList = redisTemplate.opsForValue().get(postEventDto.getPostWriterId().toString());
        // JSON 데이터를 AData 객체로 변환
        switch (postEventDto.getType()) {
            // feign client로 userId의 follower를 받음
            case "CREATED":
                //follower api로 받음
                log.info(followersList.toString());
                log.info("-----------------------------------------------------");
                followersList.getFollowersList().stream()
                        .map(Object::toString)  // Long 타입의 ID를 문자열로 변환하여 키로 사용
                        .forEach(followerId ->                {
                            ArrayList<String> currentList = redisTemplate.opsForValue().get(followerId);
                            currentList.add(postId);
                            redisTemplate.opsForValue().set(followerId, currentList);
                            }
                        );

                mycurrentList.add(postId);
                redisTemplate.opsForValue().set(postEventDto.getPostWriterId().toString(), mycurrentList);

                redisTemplate.opsForValue().get("2").stream().forEach(a->log.info(a));
                redisTemplate.opsForValue().get("3").stream().forEach(a->log.info(a));
                // key == follow Id 인 redis에 postId를 넣음
                break;
            case "DELETED":
                followersList.getFollowersList().stream()
                    .map(Object::toString)  // Long 타입의 ID를 문자열로 변환하여 키로 사용
                    .forEach(followerId ->                {
                        ArrayList<String> currentList = redisTemplate.opsForValue().get(followerId);
                        if (currentList != null && currentList.contains(postId)) {
                            currentList.remove(postId);

                            // 업데이트된 리스트를 Redis에 다시 저장
                            redisTemplate.opsForValue().set(followerId, currentList);
                        }
                    });

                mycurrentList.add(postId);
                redisTemplate.opsForValue().set(postEventDto.getPostWriterId().toString(), mycurrentList);
                // key == follow Id 인 redis에 postId를 지음
                break;
        }
    }

    @KafkaListener(topics = "user-event", groupId = "newsfeed-service-group")//확인 필요
    public void listenUserEvent(String userId) {
        // redis에 key:userId, value=[] 생성
        log.info(userId);
        String key = userId;
        ArrayList<String> emptyList = new ArrayList<>();  // 빈 리스트 생성
        redisTemplate.opsForValue().set(key, emptyList);
    }

}
