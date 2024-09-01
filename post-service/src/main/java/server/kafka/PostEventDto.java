package server.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class PostEventDto {
    private String type;
    private String postId;
    private Long postWriterId;
}
