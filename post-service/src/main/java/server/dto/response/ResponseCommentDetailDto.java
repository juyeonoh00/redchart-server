package server.dto.response;

import lombok.Getter;
import server.feign.server.ServerUsertDto;
import server.domain.Comment;

import java.time.LocalDateTime;

@Getter
public class ResponseCommentDetailDto {
    private Long writerId;
    private LocalDateTime updateDate;
    private String writername;
    private String profileImage;
    private String content;
    private Boolean isWriter;

    public ResponseCommentDetailDto(Comment comment, ServerUsertDto user, Long userId) {
        this.writerId = comment.getWriterId();
        this.updateDate = comment.getUpdateDate();
        this.writername = user.getUsername();
        this.profileImage = user.getProfileImage();
        this.content = comment.getContent();
        if (comment.getWriterId().equals(userId)){
            this.isWriter = true;
        } else {
            this.isWriter = false;
        }
    }

}
