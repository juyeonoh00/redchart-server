package server.service;

import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import server.domain.Post;
import server.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {
//
//    private PostRepository postRepository;
//
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    public Post createPost(Post post) {
//        Post savedPost = postRepository.save(post);
//        sendPostUpdate(savedPost, PostOperation.CREATE);
//        return savedPost;
//    }
//
//    public Post updatePost(Post post) {
//        Post updatedPost = postRepository.save(post);
//        sendPostUpdate(updatedPost, PostOperation.UPDATE);
//        return updatedPost;
//    }
//
//    public void deletePost(Long postId) {
//        Post post = postRepository.findById(postId).orElseThrow();
//        postRepository.delete(post);
//        sendPostUpdate(post, PostOperation.DELETE);
//    }
//
//    private void sendPostUpdate(Post post, PostOperation operation) {
//        PostUpdate postUpdate = new PostUpdate(post, operation);
//        kafkaTemplate.send("post-service-topic", serializePostUpdate(postUpdate));
//    }
}