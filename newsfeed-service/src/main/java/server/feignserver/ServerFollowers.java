package server.feignserver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface ServerFollowers {

    @GetMapping("/users/details/{userId}")
    ServerFollowersDto getFollowersById(@PathVariable("userId") Long userId);

}
