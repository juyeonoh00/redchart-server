package server.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.domain.Post;


@Getter
@NoArgsConstructor
public class RequestPostDto {

    @NotNull(message = "사용자 정보가 null입니다")
    private Long userId;

//    @NotBlank(message = "해당 댓글의 유저 정보가 null 입니다.")
//    private Long writerId;

    @Size(max = 30, message = "제목은 최대 30글자 입니다.")
    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @Size(max = 100, message = "내용은 최대 30글자 입니다.")
    @NotNull(message = "내용을 입력해주세요.")
    private String content;

    public RequestPostDto(String userId, String title, String content) {
        this.userId = Long.valueOf(userId);
//        this.writerId = writerId;
        this.title = title;
        this.content = content;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .writerId(userId)
                .content(content)
                .build();
    }
}
