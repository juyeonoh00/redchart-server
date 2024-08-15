package server.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.Follow;
import server.domain.User;
import server.dto.follow.FollowDto;
import server.dto.follow.FollowerDto;
import server.dto.follow.FollowingDto;
import server.repository.FollowRepository;
import server.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    // follow 추가
    @Transactional
    public FollowDto follow(FollowDto followDto) throws RuntimeException {
        User user = userRepository.findById(followDto.getUser_id()).orElseThrow(RuntimeException::new);
        User followee = userRepository.findById(followDto.getFollowee_id()).orElseThrow(RuntimeException::new);
        Follow follow = new Follow(user, followee);
        followRepository.save(follow);
        return new FollowDto(follow.getFollowing().getId(), follow.getFollower().getId());
    }

    // 리스트 가져오기
    public FollowerDto followers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return FollowerDto.toDto(user);
    }

    public FollowingDto following(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return FollowingDto.toDto(user);
    }
}