package server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import server.feignserver.ServerUser;
import server.domain.Comment;
import server.domain.Post;
import server.kafka.PostEventDto;
import server.dto.response.ResponseCommentDetailDto;
import server.dto.response.ResponsePostDto;
import server.dto.request.RequestPostDto;
import server.feignserver.ServerUsertDto;
import server.exception.NotMatchWriterException;
import server.repository.CommentRepository;
import server.repository.LikesRepository;
import server.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final ServerUser serverUser;
    private final KafkaTemplate<String, PostEventDto> kafkaTemplate;
    @Autowired
    public PostService(PostRepository postRepository, LikesRepository likesRepository, CommentRepository commentRepository, KafkaTemplate<String, PostEventDto> kafkaTemplate, ServerUser serverUser) {
        this.postRepository = postRepository;
        this.likesRepository = likesRepository;
        this.commentRepository = commentRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.serverUser = serverUser;
    }


    public void createpost(RequestPostDto request, Long userId) {
        Post post = request.toEntity(userId);
        postRepository.save(post);
        kafkaTemplate.send("post-event", new PostEventDto("CREATED", post.getId().toString()));
    }
    public void updatepost(RequestPostDto request, Long postId,Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        if (post.getId() != null && post.getWriterId().equals(userId)){
            post.updatePost(request.getContent(),request.getTitle());
            postRepository.save(post);
        }
        else {
            throw new NotMatchWriterException();
        }
    }

    public List<ResponseCommentDetailDto> getCommentsWithUserDetails(Long postId, Long userId){
        List<Comment> comments = commentRepository.findByPostId(postId);

        // 댓글과 사용자 정보를 결합하여 CommentResponse 생성
        return comments.stream()
                .map(comment -> {
                    // Feign Client를 사용해 User 정보 가져오기
                    ServerUsertDto user = serverUser.getUserById(comment.getWriterId());
                    // CommentResponse에 댓글 내용과 사용자 정보 추가
                    return new ResponseCommentDetailDto(
                            comment, user, userId
                    );
                })
                .collect(Collectors.toList());
    }

    public ResponsePostDto detailspost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        postRepository.save(post);
        Long likeCnt = likesRepository.countByPostId(postId);
        List<ResponseCommentDetailDto> commentList = getCommentsWithUserDetails(postId, userId);
        Boolean checkLike = likesRepository.existsByPostIdAndWriterId(postId, userId);
        // 내가 좋아요를 눌렀는지
        return new ResponsePostDto(post, userId, likeCnt, commentList, checkLike);
    }
    public void deletepost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        if (post.getId() != null && post.getWriterId().equals(userId)){
            kafkaTemplate.send("post-event", new PostEventDto("DELETED",postId.toString()));
            postRepository.deleteById(postId);
        }
        else {
            throw new NotMatchWriterException();
        }
    }


    // 카프카 테스트
//    @KafkaListener(topics = "user-service-to-post-service-data-topic", groupId = "user-service-group")
//    public void  listenAData(String dataJson) throws JsonProcessingException {
//        // JSON 데이터를 AData 객체로 변환
////        Map<String, Object> dataMap = new ObjectMapper().readValue(dataJson, new TypeReference<Map<String, Object>>() {});
//        processData(dataJson);
//    }
//    public void processData(String dataMap) {
//        A dto = new A("dataMap");
//        log.info("---------------------------------------------------------------------??");
//        log.info(String.valueOf(dto.getDtoId()));
//    }

}
