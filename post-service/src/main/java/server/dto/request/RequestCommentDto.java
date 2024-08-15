package server.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.domain.Comment;
import server.domain.Post;

@Getter
@NoArgsConstructor
public class RequestCommentDto {

    @NotNull(message = "사용자 정보가 null 입니다")
    private Long userId;

//    @NotBlank(message = "해당 댓글의 유저 정보가 null 입니다.")
//    private Long writerId;

    @Size(max = 100, message = "내용은 최대 30글자 입니다.")
    @NotNull(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "comment에 해당하는 게시물을 찾을 수 없습니다.")
    private Long postId;


    public Comment toEntity(Post post) {
        return Comment.builder()
                .writerId(userId)
                .content(content)
                .post(post)
                .build();
    }
}
