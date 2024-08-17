package server.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class PostEventDto {
    String type;
    String postId;
}
