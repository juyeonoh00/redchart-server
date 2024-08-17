package server.feignclient;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.dto.follow.FollowDto;
import server.dto.follow.FollowerDto;
import server.service.FollowService;
import server.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ClientController {
    private final UserService userService;
    private final FollowService followService;
    @GetMapping("/detail/{userId}")
    public ClientUserDto getUserById(@PathVariable Long userId){
        ClientUserDto user = userService.getUserById(userId);
        return user;
    }

    @GetMapping("/followers/{userId}")
    public FollowerDto getFollowersById(@PathVariable Long userId){
        FollowerDto user = followService.followers(userId);
        return user;
    }
}
