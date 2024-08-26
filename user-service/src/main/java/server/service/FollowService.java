package server.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import server.domain.Follow;
import server.domain.User;
import server.dto.follow.CountFollowDto;
import server.dto.follow.FollowUserListDto;
import server.feign.client.FollowersListDto;
import server.repository.FollowRepository;
import server.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    // follow 추가

    public void addfollow(Long userId, Long followeeId) {
        User follower = userRepository.findById(followeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다" + followeeId));
        User following = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다" + userId));
        if (!followRepository.existsByFollowingAndFollower(following, follower)){
            Follow follow = new Follow(following, follower);
            followRepository.save(follow);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    @Transactional
    public void deletefollow(Long userId, Long followeeId) {
        User follower = userRepository.findById(followeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다" + followeeId));
        User following = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다" + userId));
        if (followRepository.existsByFollowingAndFollower(following, follower)){
            followRepository.findByFollowerAndFollowing(follower, following);
            followRepository.deleteByFollowerAndFollowing(follower, following);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    // 리스트 가져오기
    public List<FollowUserListDto> getfollowings(Long userId) {
        List<Follow> follows = followRepository.findByFollowerId(userId);
        return follows.stream()
                .map(follow -> new FollowUserListDto(follow.getFollowing().getId(), follow.getFollowing().getUsername(),follow.getFollowing().getProfileImage()))
                .collect(Collectors.toList());
    }
    public List<FollowUserListDto> getfollowers(Long userId) {
        List<Follow> follows = followRepository.findByFollowingId(userId);
        return follows.stream()
                .map(follow -> new FollowUserListDto(follow.getFollower().getId(), follow.getFollower().getUsername(),follow.getFollower().getProfileImage()))
                .collect(Collectors.toList());
    }
    public FollowersListDto getfollowersId(Long userId) {
        List<Follow> follows = followRepository.findByFollowingId(userId);
        List<Long> followersList= follows.stream()
                .map(follow ->  follow.getFollower().getId())
                .collect(Collectors.toList());
        return new FollowersListDto(followersList);
    }

    public CountFollowDto countFollow(Long userId) {
        Long follower = followRepository.countByFollowerId(userId);
        Long following = followRepository.countByFollowingId(userId);
        return new CountFollowDto(following, follower);
    }

}