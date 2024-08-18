package server.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.User;
import server.dto.follow.CountFollowDto;
import server.dto.follow.FollowDto;
import server.dto.follow.FollowUserListDto;
import server.service.FollowService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/follow")
public class FollowController {

    private final FollowService followService;


    @PostMapping(value = "/add/{followeeId}")
    public ResponseEntity<?> addfollow(@RequestHeader("userId") Long userId, @PathVariable("followeeId") Long followeeId) {
        followService.addfollow(userId, followeeId);
        return ResponseEntity.status(HttpStatus.OK).body("팔로우가 추가 되었습니다");
    }
    @PostMapping(value = "/delete/{followeeId}")
    public ResponseEntity<?> deletefollow(@RequestHeader("userId") Long userId, @PathVariable("followeeId") Long followeeId) {
        followService.deletefollow(userId, followeeId);
        return ResponseEntity.status(HttpStatus.OK).body("팔로우가 취소 되었습니다");
    }

    @PostMapping(value = "/following/{userId}")
    public List<FollowUserListDto> getfollowings(@PathVariable Long userId) {
        return followService.getfollowers(userId);
    }
    @PostMapping(value = "/follower/{userId}")
    public List<FollowUserListDto> getfollowers(@PathVariable Long userId) {
        return followService.getfollowings(userId);
    }

    @PostMapping(value = "/num/{userId}")
    public CountFollowDto countFollow(@PathVariable Long userId) {
        return followService.countFollow(userId);
    }
// 중복 추가 안되게 수정
}
