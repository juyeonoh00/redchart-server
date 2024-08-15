package server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import server.domain.A;
import server.domain.Comment;
import server.domain.Post;
import server.dto.response.PostResponseDto;
import server.dto.request.RequestPostDto;
import server.exception.NotMatchWriterException;
import server.repository.CommentRepository;
import server.repository.LikesRepository;
import server.repository.PostRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;


    public void createpost(RequestPostDto request) {
        postRepository.save(request.toEntity());
    }
    public void updatepost(RequestPostDto request, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        if (post.getId() != null && post.getWriterId().equals(request.getUserId())){
            post.updatePost(request.getContent(),request.getTitle());
            postRepository.save(post);
        }
        else {
            throw new NotMatchWriterException();
        }
    }

    public PostResponseDto detailspost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        postRepository.save(post);

        // 좋아요와 댓글을 보여줘야함
        Long likeCnt = likesRepository.countByPostId(postId);
        List<Comment> commentList = commentRepository.findByPostId(postId);
        Boolean checkLike = likesRepository.existsByPostIdAndWriterId(postId, userId);
        // 내가 좋아요를 눌렀는지
        return new PostResponseDto(post, userId, likeCnt, commentList, checkLike);
    }
    public void deletepost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        if (post.getId() != null && post.getWriterId().equals(userId)){
            postRepository.deleteById(postId);
        }
        else {
            throw new NotMatchWriterException();
        }
    }


    // 카프카 테스트
    @KafkaListener(topics = "user-service-to-post-service-data-topic", groupId = "user-service-group")
    public void  listenAData(String dataJson) throws JsonProcessingException {
        // JSON 데이터를 AData 객체로 변환
//        Map<String, Object> dataMap = new ObjectMapper().readValue(dataJson, new TypeReference<Map<String, Object>>() {});
        processData(dataJson);
    }
    public void processData(String dataMap) {
        A dto = new A("dataMap");
        log.info("---------------------------------------------------------------------??");
        log.info(String.valueOf(dto.getDtoId()));
    }

}
