package server.feignserver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface ServerUser {

    @GetMapping("/users/details/{userId}")
    ServerUsertDto getUserById(@PathVariable("userId") Long userId);

}
