package server.feign.server;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserController {

    @GetMapping("/users/followers/{userId}")
    FollowersListDto getFollowersById(@PathVariable("userId") Long userId);

    @GetMapping("/users/detail/{userId}")
    ClientUserDto getUserById(@PathVariable("userId") Long userId);

}
