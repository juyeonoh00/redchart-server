package server.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.domain.Follow;
import server.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowUserListDto {
    private Long user_id;
    private String username;
    private String profileImage;

}