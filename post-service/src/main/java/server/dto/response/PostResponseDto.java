package server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.domain.Comment;
import server.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostResponseDto {

    private Long writerId;
    private boolean isWriter;
    private String title;
    private String content;
    private LocalDateTime updateDate;
    private Long likeCnt;
    // comment dto로 변경
    private List<Comment> commentList;
    private boolean checkLike;
    // writer의 정보를 가져와야함
    public PostResponseDto(Post post, Long userId, Long likeCnt, List<Comment> commentList, Boolean checkLike) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writerId = post.getWriterId();
        this.updateDate = post.getUpdateDate();
        this.likeCnt = likeCnt;
        this.commentList = commentList;
        this.checkLike = checkLike;
        if (post.getId().equals(userId)) {
            this.isWriter = true;
        } else {
            this.isWriter = false;
        }

    }
}
