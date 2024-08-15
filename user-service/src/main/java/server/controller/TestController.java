package server.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.domain.User;
import server.dto.follow.FollowDto;
import server.dto.follow.FollowerDto;
import server.dto.follow.FollowingDto;
import server.repository.FollowRepository;
import server.repository.UserRepository;
import server.service.FollowService;
import server.service.UserService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final FollowRepository followRepository;
    private final FollowService followService;


    @PostMapping("/signIn")
    public User signIn(@RequestBody String num) throws JsonProcessingException {
        User user = new User("email","username");
        User user2 = new User("email2","username2");
        userRepository.save(user);
        userRepository.save(user2);
        userService.sendDataToKafka(user);
        return user;
    }

    @PostMapping(value = "/follow")
    public FollowDto follow(@RequestBody FollowDto followDto) {
        return followService.follow(followDto);
    }

    @PostMapping(value = "/follower")
    public FollowerDto follower(@RequestBody Long user_id) {
        return followService.followers(user_id);
    }
    @PostMapping(value = "/following")
    public FollowingDto following(@RequestBody Long user_id) {
        return followService.following(user_id);
    }


}
