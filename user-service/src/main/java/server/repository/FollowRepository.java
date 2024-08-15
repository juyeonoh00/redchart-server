package server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.domain.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}