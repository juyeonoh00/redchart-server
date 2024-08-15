package server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.Likes;
import server.domain.Post;
import server.repository.LikesRepository;
import server.repository.PostRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikesService {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    public void addlike(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        Likes likes = new Likes(userId, post);
        likesRepository.save(likes);
    }

    public void deletelike(Long postId, Long userId) {
        Optional<Likes> likeOptional = likesRepository.findByWriterIdAndPostId(userId, postId);
        // 좋아요가 존재하면 삭제
        if (likeOptional.isPresent()) {
            likesRepository.delete(likeOptional.get());
        }
    }
}
