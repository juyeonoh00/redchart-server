package server.feign.server;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import server.dto.NewsfeedDTO;

import java.util.List;

@FeignClient(name = "post-service")
public interface PostController {

    @GetMapping("/posts/details/all")
    NewsfeedDTO getFollowersById(@RequestBody List<Long> requestDto);

}
