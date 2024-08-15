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
public class FollowingDto {
    private Long user_id;
    private List<Long> followee_id;
    public static FollowingDto toDto(User user) {
        return new FollowingDto(user.getId(), user.getFollowings().stream().map(follow -> follow.getFollower().getId()).collect(Collectors.toList()));
    }
}