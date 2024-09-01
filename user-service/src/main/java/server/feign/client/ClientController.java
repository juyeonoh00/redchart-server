package server.feign.client;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.service.FollowService;
import server.service.UserService;

import java.util.List;

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
    public FollowersListDto getFollowersById(@PathVariable Long userId){
        FollowersListDto followersId = followService.getfollowersId(userId);
        return followersId;
    }
}
