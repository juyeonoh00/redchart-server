package server.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowerDto {
    private Long user_id;
    private List<Long> follower_id;
    public static FollowerDto toDto(User user) {
        return new FollowerDto(user.getId(), user.getFollowers().stream().map(follow -> follow.getFollowing().getId()).collect(Collectors.toList()));
    }
}