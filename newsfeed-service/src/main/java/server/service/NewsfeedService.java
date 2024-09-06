package server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import server.dto.NewsfeedDTO;
import server.feign.server.PostController;

@Service
@RequiredArgsConstructor
public class NewsfeedService {

    private PostController postController;
    private RedisTemplate<String, String> redisTemplate;


    public NewsfeedDTO getNewsfeedDetails(Long userId) {
        // redis에서 userId의 postId리스트를 꺼내옴
        // post에 api 요청
        return new NewsfeedDTO();
    }
}